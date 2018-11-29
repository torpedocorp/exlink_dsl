/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe mas project licenses this file to you under the Apache License,     
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:                   
 *                                                                              
 *   http://www.apache.org/licenses/LICENSE-2.0                                 
 *                                                                              
 * Unless required by applicable law or agreed to in writing, software          
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the     
 * License for the specific language governing permissions and limitations      
 * under the License.                                                           
 */ 

package kr.co.bizframe.exlink.parser.file.plugin;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.model.ExFileBlockInfos;
import kr.co.bizframe.exlink.model.ExFileBlockInfos.BlockInfo;
import kr.co.bizframe.exlink.model.ExFileInfo;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf.MapField;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.file.AbstractFileParser;
import kr.co.bizframe.exlink.type.InterfaceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ByteIndicatorFileParser extends AbstractFileParser{


	public ByteIndicatorFileParser() {
	}

	public static Logger logger = LoggerFactory.getLogger(ByteIndicatorFileParser.class);

	

	@Override
	public LinkedHashMap<String ,ArrayList<LinkedHashMap<String, Object>>> parse(ExFileInfo info) throws ExlinkDslException{
		logger.debug("parsing start====");
		long startTime = System.currentTimeMillis();
		ExFileReaderInputStream is = null;
		ByteArrayOutputStream tempBaos = new ByteArrayOutputStream();
		LinkedHashMap<String ,ArrayList<LinkedHashMap<String, Object>>> parseInfo = new LinkedHashMap<String ,ArrayList<LinkedHashMap<String, Object>>>();
		String indicator = "000";
		String interfaceId = info.getInterfaceId();
		byte[] rowdata = null;
		try {
			is = new ExFileReaderInputStream(info.getDataFile());
			boolean isHeader = true;
			
			FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(InterfaceType.getTypeById(interfaceId));
			FileParserMapConf mapConf = null;
			while (is.isReadable()) {
				if(isHeader){
					byte data = is.readByte();
					//'\n' 개행문자인 경우
					if (data == 10) {
						byte[] headerbyte = tempBaos.toByteArray();
						mapConf = metaConf.getMapConfByFidAndLength(indicator, headerbyte.length+"").getMapConf();
//						logger.debug("mapConf = "+mapConf);

						LinkedHashMap<String, Object> dataMap = readField(headerbyte,  mapConf);
						tempBaos.close();
						isHeader = false;
						String key = mapConf.getInterfaceId()+"_"+mapConf.getFileId()+"_"+mapConf.getLengthByte();
						
						//large file could not store in memory  (heap dump caseed)
						addDataToParseInfo(parseInfo, dataMap, key);
					}else{
						tempBaos.write(data);
					}
				}else{
					tempBaos = new ByteArrayOutputStream();
					byte[] indicatorByte = is.readByte(3);
					indicator = new String(indicatorByte);
//					logger.debug(" indicator"+indicator);
					if(indicator.equals("EOF")) {
						break;
					}
					mapConf = metaConf.getMapConfByFileId(indicator).getMapConf();
					int readsize = Integer.parseInt(mapConf.getLengthByte());
					rowdata = is.readByte(readsize-3);
					tempBaos.write(indicatorByte);
					tempBaos.write(rowdata);
//					logger.debug(indicator +" data =["+new String(tempBaos.toByteArray())+"]");
					LinkedHashMap<String, Object> dataMap = readField(tempBaos.toByteArray(),  mapConf);
					String key = mapConf.getInterfaceId()+"_"+mapConf.getFileId()+"_"+mapConf.getLengthByte();
//					logger.debug(key +" of dataMap="+dataMap);
					//large file could not store in memory  (heap dump caseed)
					addDataToParseInfo(parseInfo, dataMap, key);
					
					//new line read ...
					is.readByte();
					tempBaos.close();
					tempBaos = new ByteArrayOutputStream();
					//read line feed to do nothing
				}
								
			}
			is.close();
			if(tempBaos != null) tempBaos.close();
			////////////////////////////////////////////////////////////////
			long endTime = System.currentTimeMillis();
		} catch (Exception e) {
			try {
				if(is != null) is.close();
				if(tempBaos != null) tempBaos.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			logger.error("parsing error indicator=["+indicator+"] data="+new String(rowdata));
			logger.error("★★★★★★★★★★★     "+info.getDataFile().getName()+" parsing fail   ★★★★★★★★★★★");
			throw new ExlinkDslException("read row data parsing error"+e.toString(), e);
		}
		return parseInfo;
	}

	public static void main(String args[]){

		ByteIndicatorFileParser parser = new ByteIndicatorFileParser();

		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exlink.xml");
			File dir = new File("misc/doc/ESB_DATA/test");
//			for(File file : dir.listFiles()){
//				parser.process(file);
//			}

//			File large = new File(dir, "I022608159999199_IL09001000_B07000010_001948");
			File large = new File(dir, "I010408159999199_IL09001000_B07000010_002113");
			parser.process(large);
//			System.exit(-1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			System.exit(-1);
			
		}
	}


}
