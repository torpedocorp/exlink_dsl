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

package kr.co.bizframe.exlink;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExlinkConfig {

	private static String mapDir;
	private static String dsFilename;
	private static String sendFileDir;
	
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("mapDir="+mapDir);
		sb.append(" dsFilename="+dsFilename);
		sb.append(" sendFileDir="+sendFileDir);
		return sb.toString();
	}

	public static String getMapDir() {
		return mapDir;
	}

	public static String getDsFilename() {
		return dsFilename;
	}

	public static void setMapDir(String mapDir) {
		ExlinkConfig.mapDir = mapDir;
	}

	public static void setDsFilename(String dsFilename) {
		ExlinkConfig.dsFilename = dsFilename;
	}

	public static String getSendFileDir() {
		return sendFileDir;
	}

	public static void setSendFileDir(String sendFileDir) {
		ExlinkConfig.sendFileDir = sendFileDir;
	}

}
