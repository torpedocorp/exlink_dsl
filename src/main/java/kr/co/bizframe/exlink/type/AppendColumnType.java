package kr.co.bizframe.exlink.type;

import kr.co.bizframe.exlink.ExlinkDslException;

public enum AppendColumnType {
	
	IF_FILE_NAME("IF_FILE_NAME", "인터페이스 파일명"),
	IF_SYNC_YN("IF_SYNC_YN", "데이터 처리 유뮤(Y/N)");
	
	
	
	
	private String code;
	private String desc;
	
	AppendColumnType(String code, String desc){
		this.code = code;
		this.desc = desc;
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
