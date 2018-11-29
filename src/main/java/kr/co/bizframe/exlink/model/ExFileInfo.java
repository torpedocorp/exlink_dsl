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

package kr.co.bizframe.exlink.model;
import java.io.File;

public class ExFileInfo{
	
	
	private File dataFile;
	
	private String jobCode;
	
	private String brenchCode;
	
	private String mmdd;
	
	private String hhmm;
	
	private String fileId;

	private String systemCode;
	
	private String sendCode;
	
	private String recvCode;
	
	private String interfaceId;
	
	private String fileSeq;
	
	private ExFileBlockInfos blockInfos = new ExFileBlockInfos();

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getBrenchCode() {
		return brenchCode;
	}

	public void setBrenchCode(String brenchCode) {
		this.brenchCode = brenchCode;
	}

	public String getMmdd() {
		return mmdd;
	}

	public void setMmdd(String mmdd) {
		this.mmdd = mmdd;
	}

	public String getHhmm() {
		return hhmm;
	}

	public void setHhmm(String hhmm) {
		this.hhmm = hhmm;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getSendCode() {
		return sendCode;
	}

	public void setSendCode(String sendCode) {
		this.sendCode = sendCode;
	}

	public String getRecvCode() {
		return recvCode;
	}

	public void setRecvCode(String recvCode) {
		this.recvCode = recvCode;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}
	
	public String getFileSeq() {
		return fileSeq;
	}

	public void setFileSeq(String fileSeq) {
		this.fileSeq = fileSeq;
	}

	public ExFileBlockInfos getBlockInfos() {
		return blockInfos;
	}

	public void setBlockInfos(ExFileBlockInfos blockInfos) {
		this.blockInfos = blockInfos;
	}
	
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("dataFile="+this.dataFile.getName());
		sb.append("\njobCode="+this.jobCode);
		sb.append(" brenchCode="+this.brenchCode);
		sb.append(" mmdd="+this.mmdd);
		sb.append(" hhmm="+this.hhmm);
		sb.append(" fileId="+this.fileId);
		sb.append(" systemCode="+this.systemCode);
		sb.append(" sendCode="+this.sendCode);
		sb.append(" recvCode="+this.recvCode);
		sb.append(" interfaceId="+this.interfaceId);
		sb.append(" fileSeq="+this.fileSeq);
		sb.append(" \n");
		sb.append(this.blockInfos);
		
		return sb.toString();
	}

}
