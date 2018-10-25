package kr.co.bizframe.exlink.type;

import kr.co.bizframe.exlink.ExlinkDslException;

public enum GeneratorType {
	
	Json("Json", "JsonTransform"),
	Cvs("Cvs", "CsvTransform");
	
	private String code;
	private String className;
	
	GeneratorType(String code, String className){
		this.code = code;
		this.className = className;
	}

	

	public static String getClassName(String code) throws ExlinkDslException {
		for(GeneratorType testType : values()){
			if(code.equalsIgnoreCase(testType.code)){
				return testType.getClassName();
			}
		}
		throw new ExlinkDslException("The input code["+code+"] dose not exist in MOFUserType.");
	}
	

	public static GeneratorType getTypeByCode(String code) throws ExlinkDslException {
		for(GeneratorType testType : values()){
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
