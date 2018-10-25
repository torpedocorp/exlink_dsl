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
