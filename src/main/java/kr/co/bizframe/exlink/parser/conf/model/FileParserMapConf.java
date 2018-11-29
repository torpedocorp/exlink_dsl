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

package kr.co.bizframe.exlink.parser.conf.model;

import java.util.ArrayList;


public class FileParserMapConf {
	
	private String fileId;
	private String name;
	private String interfaceId;
	private String lengthByte;
	private String sourceSystem;
	private String targetSystem;
	private String jobType;
	private String jobCycle;
	private String tableName;
	
	private ArrayList<MapField> fields = new ArrayList<MapField>();
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("fileId="+fileId);
		sb.append(" name="+name);
		sb.append(" interfaceId="+interfaceId);
		sb.append(" lengthByte="+lengthByte);
		sb.append(" sourceSystem="+sourceSystem);
		sb.append(" jobCycle="+jobCycle);
		sb.append(" jobType="+jobType);
		sb.append(" tableName="+tableName);
//			sb.append("\nfield="+field.toString());
//		}
		return sb.toString();
	}

	public String getFileId() {
		return fileId;
	}


	public void setFileId(String fileId) {
		this.fileId = fileId;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getInterfaceId() {
		return interfaceId;
	}




	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}




	public String getLengthByte() {
		return lengthByte;
	}




	public void setLengthByte(String lengthByte) {
		this.lengthByte = lengthByte;
	}




	public String getSourceSystem() {
		return sourceSystem;
	}




	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}




	public String getTargetSystem() {
		return targetSystem;
	}




	public void setTargetSystem(String targetSystem) {
		this.targetSystem = targetSystem;
	}




	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobCycle() {
		return jobCycle;
	}




	public void setJobCycle(String jobCycle) {
		this.jobCycle = jobCycle;
	}


	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ArrayList<MapField> getFields() {
		return fields;
	}




	public void setFields(ArrayList<MapField> fields) {
		this.fields = fields;
	}
	
	public void addFields(MapField fields) {
		this.fields.add(fields);
	}


	
	public String getConfId(){
		return this.getInterfaceId()+"_"+this.getFileId()+"_"+this.getLengthByte();
	}
	
	public String getDefaultTableName(){
		return this.getInterfaceId()+"_"+this.getFileId()+"_"+this.getLengthByte();
	}

	
	


	public class MapField{
		private String order;
		private String nameKor; 
		private String nameEng;
		private String type;
		private int length;
		private String desc;
		private String isPK;
		
		public String getOrder() {
			return order;
		}




		public void setOrder(String order) {
			this.order = order;
		}




		public String getNameKor() {
			return nameKor;
		}




		public void setNameKor(String nameKor) {
			this.nameKor = nameKor;
		}




		public String getNameEng() {
			return nameEng;
		}




		public void setNameEng(String nameEng) {
			this.nameEng = nameEng;
		}




		public String getType() {
			return type;
		}




		public void setType(String type) {
			this.type = type;
		}

		
		



		public int getLength() {
			return length;
		}




		public void setLength(int length) {
			this.length = length;
		}




		public String getDesc() {
			return desc;
		}




		public void setDesc(String desc) {
			this.desc = desc;
		}
		
		public String getIsPK() {
			return isPK;
		}

		public void setIsPK(String isPK) {
			this.isPK = isPK;
		}




		public String toString(){
			StringBuffer sb = new StringBuffer();
			sb.append("order="+order);
			sb.append(" nameKor="+nameKor);
			sb.append(" nameEng="+nameEng);
			sb.append(" type="+type);
			sb.append(" length="+length);
			sb.append(" isPK="+isPK);
			sb.append(" desc="+desc);
			return sb.toString();
		}
		
		
	}
	
	
	

}
