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

package kr.co.bizframe.exlink.test;

import java.io.File;
import java.io.FilenameFilter;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserMapConfing;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConfInfos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLoadConfAndParseData {
	
	public static Logger logger = LoggerFactory.getLogger(TestLoadConfAndParseData.class);

	public static FileParserMapConfInfos confInfos = new FileParserMapConfInfos();
	
	public void loadScript(String dirPath) throws Exception{
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				return fileName.endsWith(".xml");
			}
		};
		
		FileParserMapConfing parser = new FileParserMapConfing();
		
		File dir = new File(dirPath);
		if(!dir.exists()) throw new ExlinkDslException("Map script dir ["+dir.getAbsolutePath()+"] dose not exist..");
		logger.debug(dir.getAbsolutePath());
		for(File confFile : dir.listFiles(filter)){
			logger.debug(confFile.getAbsolutePath());
			FileParserMapConf conf = parser.parse(confFile);
//			confInfos.addConf(conf.getInterfaceId(), conf.getLengthByte(), conf);
		}
	}

	public static void main(String args[]){
		TestLoadConfAndParseData parser = new TestLoadConfAndParseData();
		try {
			parser.loadScript("misc/mapScript/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static FileParserMapConfInfos getConfInfos() {
		return confInfos;
	}

	public static void setConfInfos(FileParserMapConfInfos confInfos) {
		TestLoadConfAndParseData.confInfos = confInfos;
	}
	
	

}
