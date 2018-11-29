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

package kr.co.bizframe.exlink.parser.file.plugin;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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


public class ByteBlockFileParser extends AbstractFileParser{
	
	
	public ByteBlockFileParser() {
	}

	public static Logger logger = LoggerFactory.getLogger(ByteBlockFileParser.class);
	
	public void parseBlockHeader(LinkedHashMap<String ,ArrayList<LinkedHashMap<String, Object>>> parseInfo, ExFileInfo info, BlockInfo block) throws Exception{
		
		String rowData = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(block.getBlockData()), "euc-kr"));
		while ((rowData = in.readLine()) != null) {
			byte[] rowByte = rowData.getBytes("euc-kr");
			int bytesize = rowByte.length;
			
			String interfaceId = info.getInterfaceId();
			FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(InterfaceType.getTypeById(interfaceId));
			FileParserMapConf mapConf = metaConf.getMapConfByFidAndLength(info.getFileId(), bytesize+"").getMapConf();
			
			LinkedHashMap<String, Object> dataMap = readField(rowByte,  mapConf);
			String key = mapConf.getInterfaceId()+"_"+mapConf.getFileId()+"_"+mapConf.getLengthByte();
			addDataToParseInfo(parseInfo, dataMap, key);
		}
	}
	
	public void parseBlock(LinkedHashMap<String ,ArrayList<LinkedHashMap<String, Object>>> parseInfo,  ExFileInfo info, BlockInfo block) throws Exception{
		byte[] blockBytes = block.getBlockData();
		FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(InterfaceType.getTypeById(info.getInterfaceId()));
		FileParserMapConf mapConf = metaConf.getMapConfByFidAndLength(info.getFileId(), block.getReadsize()+"").getMapConf();
		int pos = 0;
		while(pos <= block.getBlocksize()-1){
			byte[] readBytes = Arrays.copyOfRange(blockBytes, pos, pos+block.getReadsize());
//			logger.debug(block.getBlocksize()+" start:"+pos+" end:"+(pos+block.getReadsize())+ " rowByte=["+new String(readBytes, "euc-kr")+"]");
			pos = pos+block.getReadsize()+1;
			LinkedHashMap<String, Object> dataMap = readField(readBytes,  mapConf);
			String key = mapConf.getInterfaceId()+"_"+mapConf.getFileId()+"_"+mapConf.getLengthByte();
			addDataToParseInfo(parseInfo, dataMap, key);
			
		}
	}
	
	
	@Override
	public LinkedHashMap<String ,ArrayList<LinkedHashMap<String, Object>>> parse(ExFileInfo info) throws ExlinkDslException{
		long startTime = System.currentTimeMillis();
		LinkedHashMap<String ,ArrayList<LinkedHashMap<String, Object>>> parseInfo = new LinkedHashMap<String ,ArrayList<LinkedHashMap<String, Object>>>();
		ExFileReaderInputStream is = null;
		try {
			is = new ExFileReaderInputStream(info.getDataFile());
			boolean isHeader = true;
			ExFileBlockInfos blockInfos = new ExFileBlockInfos();
			int blockIndex = 0;
//			int data = is.read();
			
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int length = 0;
			byte[] temp = {0, 0, 0, 0};
			while (is.isReadable()) {
				int data = is.readByte();
				temp[0] = temp[1];
				temp[1] = temp[2];
				temp[2] = temp[3];
				temp[3] = (byte) data;
				
				//'\n' 개행문자가 있는 경우 
				if (data == 10) {
					String boundStr = new String(Arrays.copyOfRange(temp,0,3));
					if(boundStr.equals("END")){
						byte[] blockBytes = null ;
						if(isHeader)  blockBytes = Arrays.copyOfRange(baos.toByteArray(), 0, baos.toByteArray().length-3);
						else blockBytes = Arrays.copyOfRange(baos.toByteArray(), 1, baos.toByteArray().length-3);
						//set block info
						BlockInfo binfo = blockInfos.new BlockInfo(isHeader);
						logger.debug("add block is header="+isHeader);
						InterfaceType itype = InterfaceType.getTypeById(info.getInterfaceId());
						int readsize = Integer.parseInt(itype.getOrder()[blockIndex+1]);
						binfo.setBlockData(blockBytes, readsize);
						blockInfos.addBlockInfo(binfo);
						
						baos.close();
						baos = new ByteArrayOutputStream();
						
						blockIndex++;
						isHeader = false;
					}else if(boundStr.equals("EOF")){
						logger.info("★★★★★★★★★★★     "+info.getDataFile().getName()+" block parsing success   ★★★★★★★★★★★");
						break;
					}
				} 
				baos.write((byte)data);
			}
			info.setBlockInfos(blockInfos);
//			logger.debug("block infos ="+info.getBlockInfos());
			long blockTime = System.currentTimeMillis();
			for(BlockInfo block : info.getBlockInfos().getBlockInfos()){
				if(block.isHeader()){
					this.parseBlockHeader(parseInfo, info, block);
				}else{
					this.parseBlock(parseInfo, info, block);
				}
			}
			long parseTime = System.currentTimeMillis();
			logger.info("★★★★★★★★★★★     "+info.getDataFile().getName()+" data parsing take time:"+( parseTime - blockTime));
			
			
		
			is.close();
			long endTime = System.currentTimeMillis();
			logger.info("★★★★★★★★★★★     "+info.getDataFile().getName()+" take time:"+( endTime - startTime));
		} catch (Exception e) {
			try {
				if(is != null)
					is.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			logger.error("★★★★★★★★★★★     "+info.getDataFile().getName()+" parsing fail   ★★★★★★★★★★★");
			throw new ExlinkDslException("read row data parsing error"+e.toString(), e);
		}
		return parseInfo;
	}
	
	public static void main(String args[]){
		ByteBlockFileParser parser = new ByteBlockFileParser();
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exlink.xml");
			File dir = new File("misc/doc/ESB_DATA/test");
			File large = new File(dir, "CA_20180906001505_00(RESPONSE).RLT_20180906001838");
			parser.process(large);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
