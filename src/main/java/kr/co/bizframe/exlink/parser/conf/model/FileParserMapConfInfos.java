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

package kr.co.bizframe.exlink.parser.conf.model;

import java.util.HashMap;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.model.ExFileInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileParserMapConfInfos {
	
	public static Logger logger = LoggerFactory.getLogger(FileParserMapConfInfos.class);


	private HashMap<String,  HashMap<String, FileParserMapConf>> infos = new HashMap<String,  HashMap<String, FileParserMapConf>>();
	
	public void addConf(String interfaceId, String key, FileParserMapConf conf){
		if(infos.get(interfaceId) == null){
			HashMap<String, FileParserMapConf> interfaceMap = new HashMap<String, FileParserMapConf>();
			interfaceMap.put(key, conf);
			infos.put(interfaceId, interfaceMap);
		}else{
			HashMap<String, FileParserMapConf> interfaceMap = infos.get(interfaceId);
			interfaceMap.put(key, conf);
			infos.put(interfaceId, interfaceMap);
		}
	}
	
	
	
	public HashMap<String, HashMap<String, FileParserMapConf>> getInfos() {
		return infos;
	}


	public FileParserMapConf getConf(ExFileInfo info, String bytesize) throws ExlinkDslException{
			HashMap<String, FileParserMapConf> interfaceMap = infos.get(info.getInterfaceId());
			//logger.debug("matched interfaceMap = "+ interfaceMap);
			if(interfaceMap == null) throw new ExlinkDslException("Can not find script mapping conf ["+info.getInterfaceId()+"_"+info.getFileId()+"_"+bytesize+".xml]");
			FileParserMapConf conf = interfaceMap.get(bytesize);
			//logger.debug("matched conf = "+ conf);
			if(conf == null) throw new ExlinkDslException("Can not find script mapping conf ["+info.getInterfaceId()+"_"+info.getFileId()+"_"+bytesize+".xml]");
			
			return conf;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("infos="+infos);
		return sb.toString();
	}
	

}
