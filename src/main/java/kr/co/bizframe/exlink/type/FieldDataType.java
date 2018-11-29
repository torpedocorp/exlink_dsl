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

package kr.co.bizframe.exlink.type;

import kr.co.bizframe.exlink.ExlinkDslException;

public enum FieldDataType {
	
	CHAR("CHAR", "VARCHAR2", "문자형"),
	;
	
	
	
	
	private String type;
	private String dbType;
	private String desc;
	
	FieldDataType(String type, String dbType, String desc){
		this.type = type;
		this.dbType = dbType;
		this.desc = desc;
	}

	

	public static String getDBType(String type) throws ExlinkDslException {
		for(FieldDataType ftype : values()){
			if(ftype.type.equalsIgnoreCase(ftype.type)){
				return ftype.dbType;
			}
		}
		throw new ExlinkDslException("The input type["+type+"] dose not exist in FieldDataType.");
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getDbType() {
		return dbType;
	}



	public void setDbType(String dbType) {
		this.dbType = dbType;
	}



	public String getDesc() {
		return desc;
	}



	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
