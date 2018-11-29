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

package kr.co.bizframe.exlink.handler;

import java.io.File;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.file.AbstractFileParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExlinkFileParserHandler {
	
	public static Logger logger = LoggerFactory.getLogger(ExlinkFileParserHandler.class);

	
	
	
	public static void main(String args[]){
		String className = "kr.co.bizframe.camel.dbsync.handler.DbSyncInHandler";
		logger.debug("invoke plugin class name=["+className+"]");
		try {
			ExlinkFileParserHandler plugIn = (ExlinkFileParserHandler) Class.forName(className).newInstance();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
