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
