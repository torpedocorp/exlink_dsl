package kr.co.bizframe.exlink.type;

import kr.co.bizframe.exlink.ExlinkDslException;

public enum ParsingType {
	
	Json("Json", "json type file"),
	Byte("Byte", "byte stream type file");
	
	private String code;
	private String desc;
	
	ParsingType(String code, String desc){
		this.code = code;
		this.desc = desc;
	}

	


	public static ParsingType getTypeByCode(String code) throws ExlinkDslException {
		for(ParsingType testType : values()){
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




	public String getDesc() {
		return desc;
	}




	public void setDesc(String desc) {
		this.desc = desc;
	}


}
