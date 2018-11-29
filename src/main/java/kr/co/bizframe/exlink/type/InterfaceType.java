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

public enum InterfaceType {
	
	B07000010("B07000010", "199", "통합정산연계서버(일마감)", FileParseType.Indicator, null),
	B60010010("B60010010", "401", "호환전자카드 지불", FileParseType.Block, new String[]{"69", "137", "314", "115", "97"}),
	B60020010("B60020010", "402", "후불전자카드 지불", FileParseType.Block, new String[]{"73", "121", "221", "298", "115", "96","156", "80", "90"}),
	B60060010("B60060010", "410", "신용카드 지불", FileParseType.Block, new String[]{"73", "121", "329", "295", "115", "96"}),
	CAREQ("CAREQ", "CA", "차적조회신청", FileParseType.Block, new String[]{"12"}),
	CARES("CARES", "CA", "차적조회결과", FileParseType.Block, new String[]{"840"}),
	DAYCALREQ("DAYCALREQ", "DAYCALREQ", "온라인 실시간 전문 전송", FileParseType.Block, new String[]{"12"}),
	DAYCALRES("DAYCALRES", "DAYCALRES", "온라인 실시간 전문 전송", FileParseType.Block, new String[]{"840"}),
	;
	private String interfaceId;
	private String fileId;
	private String desc;
	private FileParseType parseType;
	private String[] order;
	
	InterfaceType(String interfaceId, String fileId, String desc, FileParseType parseType, String[] order){
		this.interfaceId = interfaceId;
		this.fileId = fileId;
		this.desc = desc;
		this.parseType = parseType;
		this.order = order;
	}

	
	public String getMetaFileName() {
		return interfaceId+"_"+fileId+"_meta"+".xml";
	}
	
	public static InterfaceType getTypeById(String interfaceId) throws ExlinkDslException {
		for(InterfaceType testType : values()){
			if(interfaceId.equalsIgnoreCase(testType.interfaceId)){
				return testType;
			}
		}
		throw new ExlinkDslException("The input code["+interfaceId+"] dose not exist in InterfaceType.");
	}
	

	public static InterfaceType getTypeByFileId(String fileId) throws ExlinkDslException {
		for(InterfaceType testType : values()){
			if(fileId.equalsIgnoreCase(testType.fileId)){
				return testType;
			}
		}
		throw new ExlinkDslException("The input code["+fileId+"] dose not exist in InterfaceType.");
	}



	public String getInterfaceId() {
		return interfaceId;
	}



	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}



	public String getFileId() {
		return fileId;
	}



	public void setFileId(String fileId) {
		this.fileId = fileId;
	}



	public String getDesc() {
		return desc;
	}



	public void setDesc(String desc) {
		this.desc = desc;
	}


	public FileParseType getParseType() {
		return parseType;
	}


	public void setParseType(FileParseType parseType) {
		this.parseType = parseType;
	}


	public String[] getOrder() {
		return order;
	}


	public void setOrder(String[] order) {
		this.order = order;
	}
	
}

