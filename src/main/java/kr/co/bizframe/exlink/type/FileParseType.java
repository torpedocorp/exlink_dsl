package kr.co.bizframe.exlink.type;

import kr.co.bizframe.exlink.ExlinkDslException;

public enum FileParseType {
	
	Block("Block", "END 구문으로  block으로 나누짐"),
	Indicator("Indicator", "앞 첫번째 3byte가 인터페이스 구분자");
	
	private String code;
	private String desc;
	
	FileParseType(String code, String desc){
		this.code = code;
		this.desc = desc;
	}

	
	
	public static FileParseType getTypeByCode(String code) throws ExlinkDslException {
		for(FileParseType testType : values()){
			if(code.equalsIgnoreCase(testType.code)){
				return testType;
			}
		}
		throw new ExlinkDslException("The input code["+code+"] dose not exist in InterfaceParseType.");
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

