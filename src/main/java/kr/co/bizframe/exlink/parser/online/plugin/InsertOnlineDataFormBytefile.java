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

package kr.co.bizframe.exlink.parser.online.plugin;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf.MapField;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.type.InterfaceType;
import kr.co.bizframe.exlink.util.DBInsertUtil;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InsertOnlineDataFormBytefile {


	
	public byte[] readByte(byte[] data, int pos, int length )
	{
		byte[] transplant = new byte[length];

		System.arraycopy(data, pos, transplant, 0, length);

		return transplant;
	}
	
	protected LinkedHashMap<String, Object> readField(byte[] rowByte, FileParserMapConf mapConf) throws UnsupportedEncodingException, ExlinkDslException{
		int start = 0;
		
		LinkedHashMap<String, Object> dataMap = new LinkedHashMap<String, Object>();
		for(MapField field : mapConf.getFields()){
			int length = field.getLength();
			if(rowByte.length < (start+length)){
				throw new ExlinkDslException(mapConf+" data length"+rowByte.length +" read length="+(start+length));
			}
			byte[] fieldValueByte = readByte(rowByte, start, length);
//			logger.debug(field.getOrder()+ " : "+field.getNameKor()+ " ## "+field.getNameEng()+"="+new String(fieldValueByte, "euc-kr"));
			start = length+start;
			dataMap.put(field.getNameEng(), new String(fieldValueByte).trim());
		}
		return dataMap;
	}

	public static Logger logger = LoggerFactory.getLogger(InsertOnlineDataFormBytefile.class);

	public LinkedHashMap<String, Object> parseLine(byte[] line, InterfaceType type) throws Exception{
		byte[] blockBytes = line;
		FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
		FileParserMapConf mapConf = metaConf.getMapConfByFidAndLength(type.getFileId(), type.getOrder()[0]).getMapConf();
		LinkedHashMap<String, Object> dataMap = readField(line,  mapConf);
		String key = mapConf.getInterfaceId()+"_"+mapConf.getFileId()+"_"+mapConf.getLengthByte();
		return dataMap;
	}

	public ArrayList<LinkedHashMap<String, Object>> parse(byte[] data, InterfaceType type) throws ExlinkDslException{
		long startTime = System.currentTimeMillis();
		ArrayList<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
		ByteArrayInputStream is = null;
		BufferedReader bfReader = null;
		try {
			is = new ByteArrayInputStream(data);
			bfReader = new BufferedReader(new InputStreamReader(is, "euc-kr"));
			String temp = null;

			while((temp = bfReader.readLine()) != null){
				logger.debug(temp.getBytes("euc-kr").length+"=vs="+temp.length()+":"+temp);
				LinkedHashMap<String, Object> dataMap = parseLine(temp.getBytes(), type);
				list.add(dataMap);
			}
			is.close();
			long endTime = System.currentTimeMillis();
			logger.info("★★★★★★★★★★★     parsing take time:"+( endTime - startTime));
		} catch (Exception e) {
			try {
				if(is != null)
					is.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw new ExlinkDslException("read row data parsing error"+e.toString(), e);
		}
		return list;
	}
	
	public void makeCaresData(){
		InsertOnlineDataFormBytefile parser = new InsertOnlineDataFormBytefile();
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exhub.xml");
			File dir = new File("misc/doc/ESB_DATA/test");
			File large = new File(dir, "CA_20180906001505_00(RESPONSE).RLT_20180906001838");
			byte[] data = FileUtils.readFileToByteArray(large);
			InterfaceType type = InterfaceType.CARES;
			ArrayList<LinkedHashMap<String, Object>> list = parser.parse(data, type);
			for(LinkedHashMap<String, Object> item : list){
//				item={regCar_no=06도6461, 
//						regCar_name=투스카니(TUSCANI), 
//						regName=홍길동, regName_num=92082, 
//						regAddr=51238836경상북도 김천시 혁신8로 77(율곡동), regOk_gubun=, regSayu=00운행차량, regClose_date=, regComp=}

				logger.debug("item="+item);
				String regCar_no = (String)item.get("regCar_no");
				String regCar_name = (String)item.get("regCar_name");
				String regName = (String)item.get("regName");
				String regName_num = (String)item.get("regName_num");
				String regAddr = (String)item.get("regAddr");
				regName_num = regName_num+regAddr.substring(0, regAddr.indexOf("경"));
				regAddr = regAddr.substring(regAddr.indexOf("경"), regAddr.length());
				
				
				String regOk_gubun = (String)item.get("regSayu");
				String regSayu = (String)item.get("regSayu");
				regAddr = "경상북도 김천시 혁신8로 77(율곡동)";
//				if(regSayu.indexOf("운") > 0) {
//					regOk_gubun = regSayu.substring(0, regSayu.indexOf("운"));
//					regSayu = regSayu.substring(regSayu.indexOf("운"), regSayu.length());
//				}
				regOk_gubun = "00";
				regSayu = "운행차량";
				
				String regClose_date = (String)item.get("regClose_date");
				String regComp = (String)item.get("regComp");
				regComp = "3";
				item.put("regComp", regComp);
				logger.debug(regName_num+":"+regAddr+":"+regOk_gubun+":"+regSayu+":"+regComp);
				item.put("regName_num", regName_num);
				item.put("regAddr", regAddr);
				item.put("regOk_gubun", regOk_gubun);
				item.put("regSayu", regSayu);
				FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
				
				FileParserMapConf mapConf = metaConf.getMapConfByFidAndLength(type.getFileId(), type.getOrder()[0]).getMapConf();
				DBInsertUtil.insertItem(item, mapConf);
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void makeCareqData(){
		InsertOnlineDataFormBytefile parser = new InsertOnlineDataFormBytefile();
		try {
			InterfaceType type = InterfaceType.CAREQ;
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exlink.xml");
			File dir = new File("misc/doc/ESB_DATA/test");
			File large = new File(dir, "CA_20180906001505_00(RESPONSE).RLT_20180906001838");
			byte[] data = FileUtils.readFileToByteArray(large);
			ArrayList<LinkedHashMap<String, Object>> list = parser.parse(data, type);
		
			
			for(LinkedHashMap<String, Object> item : list){
//				item={regCar_no=06도6461, 
//						regCar_name=투스카니(TUSCANI), 
//						regName=홍길동, regName_num=92082, 
//						regAddr=51238836경상북도 김천시 혁신8로 77(율곡동), regOk_gubun=, regSayu=00운행차량, regClose_date=, regComp=}

				logger.debug("item="+item);
				String regCar_no = (String)item.get("regCar_no");
				logger.debug(regCar_no);
				item.put("regCar_no", regCar_no);
				FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
				
				FileParserMapConf mapConf = metaConf.getMapConfByFidAndLength(type.getFileId(), type.getOrder()[0]).getMapConf();
				DBInsertUtil.insertItem(item, mapConf);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]){
		InsertOnlineDataFormBytefile ins = new InsertOnlineDataFormBytefile();
		ins.makeCareqData();
	}

}
