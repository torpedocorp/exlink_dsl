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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.model.ExFileInfo;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;
import kr.co.bizframe.exlink.parser.file.AbstractFileGenerator;
import kr.co.bizframe.exlink.parser.file.AbstractFileParser;
import kr.co.bizframe.exlink.sql.SqlExecutor;
import kr.co.bizframe.exlink.sql.SqlFetcher;
import kr.co.bizframe.exlink.sql.scirpt.SqlParser;
import kr.co.bizframe.exlink.type.AppendColumnType;
import kr.co.bizframe.exlink.type.GeneratorType;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;


public class JsonFileParser extends AbstractFileParser{


	public JsonFileParser() {
	}

	public static Logger logger = LoggerFactory.getLogger(JsonFileParser.class);
	
	
	@Override
	public LinkedHashMap<String ,ArrayList<Map<String, Object>>> parse(ExFileInfo info) throws ExlinkDslException {
		
		try {
			long startTime = System.currentTimeMillis();
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(info.getDataFile()));
			LinkedHashMap<String ,ArrayList<Map<String, Object>>> data = gson.fromJson(reader, LinkedHashMap.class);
			long endTime = System.currentTimeMillis();
			logger.info("■■■■■■■■■■■■■■■ file to make object json time ="+(endTime - startTime));
			logger.debug("data class="+data.getClass());
			return data;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	

	
	
	public void jsonToObject(File file) throws FileNotFoundException{
		long startTime = System.currentTimeMillis();
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader(file));
		LinkedHashMap<String ,List<Map<String, Object>>> data = gson.fromJson(reader, LinkedHashMap.class);
		long endTime = System.currentTimeMillis();
		logger.info("■■■■■■■■■■■■■■■ file to make object json time ="+(endTime - startTime));
		HashMap tt = data;
		logger.debug(tt.getClass().getName());
		for(Object objKey : tt.keySet()){
			String key = (String)objKey;
			logger.debug(tt.get(key).getClass().getName());
			List list = (List) tt.get(key);
			for(Object itemObj : list){
				logger.debug(itemObj.getClass().getName());
				com.google.gson.internal.LinkedTreeMap ss = (com.google.gson.internal.LinkedTreeMap) itemObj;
				AbstractMap aa = (AbstractMap) itemObj;
				for(Object aakey : aa.keySet()){
					String k = (String)aakey;
					logger.debug(""+aa.get(aakey));
				}
				logger.debug(ss.getClass().getName());
				
			}
		}
	}
	
	
	public void testFileToDB(String filename){
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exhub.xml");
			File dir = new File("misc/data/send");
			File large = new File(dir, filename);
			process(large);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	
	public static void main(String args[]){
		JsonFileParser p = new JsonFileParser();
		try {
			p.testFileToDB("I022608159999199_IL09001000_B07000010_001948");
//			p.jsonToObject(new File("D:/project/open_sw_camel/exlink_dsl/misc/data/send/I022608159999199_IL09001000_B07000010_001948"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
