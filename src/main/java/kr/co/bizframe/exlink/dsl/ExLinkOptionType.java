package kr.co.bizframe.exlink.dsl;

import kr.co.bizframe.exlink.ExlinkDslException;

public enum ExLinkOptionType {
	//basic option
	mapConfDir("mapConfDir", "스크립트 패스가 있는 위치"),
	
	//sending option
	generateFormat("generateFormat", "생성 파일 형태"),
	generateDir("generateDir", "생성 파일 저장위치"),
	;
	
	private String code;
	private String desc;
	
	ExLinkOptionType(String code, String desc){
		this.code = code;
		this.desc = desc;
	}

	
	public static ExLinkOptionType getTypeByCode(String code) throws ExlinkDslException {
		for(ExLinkOptionType testType : values()){
			if(code.equalsIgnoreCase(testType.code)){
				return testType;
			}
		}
		throw new ExlinkDslException("The input code["+code+"] dose not exist in ExLinkOptionType.");
	}
	
	
	
	
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getDesc() {
		return desc;
	}



	public void setDesc(String desc) {
		this.desc = desc;
	}

	
}
