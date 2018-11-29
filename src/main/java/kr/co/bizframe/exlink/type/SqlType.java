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

package kr.co.bizframe.exlink.type;

import kr.co.bizframe.exlink.ExlinkDslException;

public enum SqlType {
	
	FETCH("FETCH", "DbSyncInHandler"),
	EXECUTE("EXECUTE", "DbSyncOutHandler");
	
	
	
	
	private String code;
	private String className;
	
	SqlType(String code, String className){
		this.code = code;
		this.className = className;
	}

	

	public static String getClassName(String code) throws ExlinkDslException {
		for(SqlType testType : values()){
			if(code.equalsIgnoreCase(testType.code)){
				return testType.getClassName();
			}
		}
		throw new ExlinkDslException("The input code["+code+"] dose not exist in MOFUserType.");
	}
	

	public static SqlType getTypeByCode(String code) throws ExlinkDslException {
		for(SqlType testType : values()){
			if(code.equalsIgnoreCase(testType.code)){
				return testType;
			}
		}
		throw new ExlinkDslException("The input code["+code+"] dose not exist in MOFUserType.");
	}
	
	
	
	
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}



	public String getClassName() {
		return className;
	}



	public void setClassName(String className) {
		this.className = className;
	}
}
