/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe exlink project licenses this file to you under the Apache License,     
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

package kr.co.bizframe.exlink.test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.model.ExFileBlockInfos;
import kr.co.bizframe.exlink.model.ExFileBlockInfos.BlockInfo;
import kr.co.bizframe.exlink.model.ExFileInfo;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf.MapField;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConfInfos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestReadFileName {
	public static Logger logger = LoggerFactory.getLogger(TestReadFileName.class);
	
	private FileParserMapConfInfos confInfos = null;

	public TestReadFileName(FileParserMapConfInfos confInfos){
		this.confInfos = confInfos;
	}
	
	
	public void readDir(File dir) throws ExlinkDslException{
		for(File file : dir.listFiles()){
			String fileName = file.getName();
			logger.debug("==============================================="+fileName);
			if(fileName.length() == 44){
				String[] list = fileName.split("_");
				ExFileInfo info = new ExFileInfo();
				info.setDataFile(file);
				info.setJobCode(list[0].substring(0, 1));
				info.setBrenchCode(list[0].substring(1, 5));
				info.setMmdd(list[0].substring(5, 9));
				info.setHhmm(list[0].substring(9, 13));
				info.setFileId(list[0].substring(13, 16));
				
				info.setSystemCode(list[1].substring(0, 2));
				info.setSendCode(list[1].substring(2, 6));
				info.setRecvCode(list[1].substring(6, 10));
				 
				info.setInterfaceId(list[2]);

				info.setFileSeq(list[3]);
				readFileBlock(info);
				
				
				logger.debug(info.toString());
				
			}else{
				logger.error("The file ["+file.getName()+"] not a interface file");
			}
			
		}
		
	}
	
	public byte[] readByte(byte[] data, int pos, int length )
	{
	    byte[] transplant = new byte[length];

	    System.arraycopy(data, pos, transplant, 0, length);

	    return transplant;
	}
	
	public ExFileBlockInfos readFileBlock(ExFileInfo info) throws ExlinkDslException{
		long startTime = System.currentTimeMillis();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(info.getDataFile()),"euc-kr"));
			String rowData;
			boolean isBlock = false;
			ExFileBlockInfos blockInfos = new ExFileBlockInfos();
			BlockInfo binfo = blockInfos.new BlockInfo(true);
			Hashtable<String, String> useconf = new Hashtable<String, String>();
			while ((rowData = in.readLine()) != null) {
				if(rowData.equals("EOF")){
					logger.info("★★★★★★★★★★★     "+info.getDataFile().getName()+" parsing success   ★★★★★★★★★★★");
				}else if(rowData.equals("END")){
					binfo = blockInfos.new BlockInfo(false);
					isBlock = true;
				}else{
//					logger.debug("rowData ="+rowData);
					int bytesize = rowData.getBytes("euc-kr").length;
					byte[] rowByte = rowData.getBytes();
					String interfaceId = info.getInterfaceId();
					//logger.debug(confInfos+" interfaceId="+interfaceId+ " bytesize"+bytesize);
					//logger.debug(" interfaceId="+interfaceId+ " bytesize"+bytesize);
					
					FileParserMapConf conf = confInfos.getConf(info, bytesize+"");
					logger.debug("conf ="+conf.getConfId());
					useconf.put(conf.getConfId(), "1");
					if(conf == null) throw new ExlinkDslException("Can not find script mapping conf"+interfaceId+"_"+conf.getFileId()+"_"+bytesize+".xml");
					
					int start = 0;
					LinkedHashMap<String, Object> dataMap = new LinkedHashMap<String, Object>();
					for(MapField field : conf.getFields()){
						int length = field.getLength();
						//logger.debug(field.getOrder()+ " : "+field.getNameKor()+ " ## "+field.getNameEng());
						byte[] fieldValueByte = readByte(rowByte, start, length);
						//logger.debug(field.getOrder()+ " : "+field.getNameKor()+ " ## "+field.getNameEng()+"="+new String(fieldValueByte));
						start = length+start;
						dataMap.put(field.getNameEng(), new String(fieldValueByte));
					}
//					logger.debug(conf.getInterfaceId()+"_"+conf.getFileId()+"_"+conf.getLengthByte()+"="+dataMap);
					
					//logger.debug(conf.toString());
				}
			}
			if(!isBlock) blockInfos.addBlockInfo(binfo);
//			logger.debug("usedconf="+useconf);
			in.close();
			info.setBlockInfos(blockInfos);
			////////////////////////////////////////////////////////////////
			long endTime = System.currentTimeMillis();
			logger.error("★★★★★★★★★★★     "+info.getDataFile().getName()+" take time:"+( endTime - startTime));
			
			return blockInfos;
		} catch (Exception e) {
			logger.error("★★★★★★★★★★★     "+info.getDataFile().getName()+" parsing fail   ★★★★★★★★★★★");
			throw new ExlinkDslException("read row data parsing error"+e.toString(), e);
		}
	}
	
	public static void main(String args[]){
		
		TestLoadConfAndParseData parser = new TestLoadConfAndParseData();
		try {
			parser.loadScript("misc/mapScript/");
			TestReadFileName rbrf = new TestReadFileName(parser.getConfInfos());
			
			File dir  = new File("misc/doc/ESB_DATA");
			rbrf.readDir(dir);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
