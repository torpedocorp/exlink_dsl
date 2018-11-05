package kr.co.bizframe.exlink.rest.model;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.co.bizframe.exlink.test.rest.Order;
import kr.co.bizframe.exlink.type.InterfaceType;


public class RestResponseModel {
	
	private InterfaceType interfaceType;
	private String  loadServiceName;
	private String fileId;
	private Date createdOn = new Date();
	private Object dataObject;
	
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("interfaceType="+interfaceType);
		sb.append(" fileId="+fileId);
		sb.append(" createdOn="+createdOn);
		sb.append(" dataObject="+dataObject);
		return sb.toString();
	}

	public InterfaceType getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(InterfaceType interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Object getDataObject() {
		return dataObject;
	}

	public void setDataObject(Object dataObject) {
		this.dataObject = dataObject;
	}
	
	public Date getCreatedOn() {
		return createdOn;
	}


	public String getLoadServiceName() {
		return loadServiceName;
	}

	public void setLoadServiceName(String loadServiceName) {
		this.loadServiceName = loadServiceName;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public RestResponseModel jsonToObject(String resMessage){
		long startTime = System.currentTimeMillis();
		
		Gson gson =  new GsonBuilder().setPrettyPrinting().create();
		RestResponseModel res = gson.fromJson(resMessage, RestResponseModel.class);
		System.out.println("res = "+res);
		return res;
	}
	
	public String toJson(){
		long startTime = System.currentTimeMillis();
		Gson gson =  new GsonBuilder().setPrettyPrinting().create();
		String data = gson.toJson(this);
		return data;
	}

}
