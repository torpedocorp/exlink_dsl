/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe exlink project licenses this file to you under the Apache License,     
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

package kr.co.bizframe.exlink.rest.model;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.co.bizframe.exlink.type.InterfaceType;


public class RestRequestModel {
	
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

	public String objectToJson(){
		long startTime = System.currentTimeMillis();
		Gson gson =  new GsonBuilder().setPrettyPrinting().create();
		String data = gson.toJson(dataObject);
		return data;
	}
	
	public String toJson(){
		long startTime = System.currentTimeMillis();
		Gson gson =  new GsonBuilder().setPrettyPrinting().create();
		String data = gson.toJson(this);
		return data;
	}

	public String getLoadServiceName() {
		return loadServiceName;
	}

	public void setLoadServiceName(String loadServiceName) {
		this.loadServiceName = loadServiceName;
	}

	

}
