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

public enum ExSystemType {
	
	IL("IL", "1000", "도공정산연계"),
	IC("IC", "900", "통합수집"),
	SS("SS", "955", "표준연계"),
	PO("PC", "OTHER", "민자"),
	;
	
	
	
	
	private String code;
	private String branchCode;
	private String desc;
	
	ExSystemType(String code, String className, String desc){
		this.code = code;
		this.branchCode = branchCode;
		this.desc = desc;
	}

	

	public static String getBranchCode(String code) throws ExlinkDslException {
		for(ExSystemType testType : values()){
			if(code.equalsIgnoreCase(testType.code)){
				return testType.getBranchCode();
			}
		}
		throw new ExlinkDslException("The input code["+code+"] dose not exist in MOFUserType.");
	}
	

	public static ExSystemType getTypeByCode(String code) throws ExlinkDslException {
		for(ExSystemType testType : values()){
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



	public String getBranchCode() {
		return branchCode;
	}



	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}


	public String getDesc() {
		return desc;
	}



	public void setDesc(String desc) {
		this.desc = desc;
	}

	
}
