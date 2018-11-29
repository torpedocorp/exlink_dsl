/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe mas project licenses this file to you under the Apache License,     
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

import java.io.File;
import java.util.ArrayList;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.type.FileParseType;

public class FileParserMetaConf {

	private String fileId;
	private String name;
	private String interfaceId;
	private FileParseType readType;
	private FileParseType writeType;
	private String postMetaAdaptor;
	
	private ArrayList<FileParserMapConfInfo> mapList = new ArrayList<FileParserMapConfInfo>();
	
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

	public FileParseType getReadType() {
		return readType;
	}

	public void setReadType(FileParseType readType) {
		this.readType = readType;
	}

	public FileParseType getWriteType() {
		return writeType;
	}

	public void setWriteType(FileParseType writeType) {
		this.writeType = writeType;
	}
	
	public String getPostMetaAdaptor() {
		return postMetaAdaptor;
	}

	public void setPostMetaAdaptor(String postMetaAdaptor) {
		this.postMetaAdaptor = postMetaAdaptor;
	}

	public ArrayList<FileParserMapConfInfo> getMapList() {
		return mapList;
	}

	public void setMapList(ArrayList<FileParserMapConfInfo> mapList) {
		this.mapList = mapList;
	}

	public void addMapList(FileParserMapConfInfo map) {
		mapList.add(map);
	}
	
	private FileParserMapConfInfo getMapConfByByteLength(String length) throws ExlinkDslException {
		for(FileParserMapConfInfo mapinfo : this.getMapList()){
			if(mapinfo.getLengthByte().equals(length)){
				return mapinfo;
			}
		}
		throw new ExlinkDslException("Map info id ["+interfaceId+"] length["+length+"] dose not exist..");
	}
	
	public FileParserMapConfInfo getHeaderInfo() throws ExlinkDslException {
		
		if(this.getMapList() == null && this.getMapList().size() <=0 )
			throw new ExlinkDslException("Map list size must be more than one..");
		return this.getMapList().get(0);
	}
	
	public FileParserMapConfInfo getMapConfByFileId(String fid) throws ExlinkDslException {
		for(FileParserMapConfInfo mapinfo : this.getMapList()){
			if(mapinfo.getFileId().equals(fid)){
				return mapinfo;
			}
		}
		throw new ExlinkDslException("Map info id ["+interfaceId+"] fid["+fid+"] dose not exist..");
	}
	
	public FileParserMapConfInfo getMapConfByFidAndLength(String fid, String length) throws ExlinkDslException {
		for(FileParserMapConfInfo mapinfo : this.getMapList()){
			if(mapinfo.getFileId().equals(fid) && mapinfo.getLengthByte().equals(length)){
				return mapinfo;
			}
		}
		throw new ExlinkDslException("Map info id ["+interfaceId+"] fid["+fid+"] and length["+length+"] dose not exist..");
	}
	
	public FileParserMapConfInfo getMapConfByMapKey(String key) throws ExlinkDslException {
		String[] list = key.split("_");
		 
		String interfaceid = list[0];
		String fid = list[1];
		String length = list[2];
		for(FileParserMapConfInfo mapinfo : this.getMapList()){
			if(mapinfo.getFileId().equals(fid) && mapinfo.getLengthByte().equals(length)){
				return mapinfo;
			}
		}
		throw new ExlinkDslException("Map info id ["+interfaceId+"] fid["+fid+"] and length["+length+"] dose not exist..");
	}


	public String toString(){
		
		StringBuffer sb = new StringBuffer();
		sb.append("fileId="+fileId);
		sb.append(" name="+name);
		sb.append(" interfaceId="+interfaceId);
		sb.append(" readType="+readType);
		sb.append(" writeType="+writeType);
		sb.append(" postMetaAdaptor="+postMetaAdaptor);
		
		for(FileParserMapConfInfo map : mapList){
			sb.append("\nmap="+map.toString());
		}
		return sb.toString();
	}

	public class FileParserMapConfInfo {
		private String order;
		private String name;
		private String lengthByte;
		private String fileId;
		private FileParseType readType;
		private FileParseType writeType;
		private String postMapItemAdaptor;
		private String postMapListAdaptor;
		private String mapFileName;
		private File file;
		private FileParserMapConf mapConf;
		
		public String toString(){
			
			StringBuffer sb = new StringBuffer();
			sb.append("order="+order);
			sb.append(" name="+name);
			sb.append(" fileId="+fileId);
			sb.append(" lengthByte="+lengthByte);
			sb.append(" readType="+readType);
			sb.append(" writeType="+writeType);
			sb.append(" mapFileName="+mapFileName);
			sb.append(" postMapItemAdaptor="+postMapItemAdaptor);
			sb.append(" postMapListAdaptor="+postMapListAdaptor);
			return sb.toString();
		}
		
		
		public String getFileId() {
			return fileId;
		}


		public void setFileId(String fileId) {
			this.fileId = fileId;
		}


		public String getOrder() {
			return order;
		}

		public void setOrder(String order) {
			this.order = order;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLengthByte() {
			return lengthByte;
		}

		public void setLengthByte(String lengthByte) {
			this.lengthByte = lengthByte;
		}

		public FileParseType getReadType() {
			return readType;
		}

		public void setReadType(FileParseType readType) {
			this.readType = readType;
		}

		public FileParseType getWriteType() {
			return writeType;
		}

		public void setWriteType(FileParseType writeType) {
			this.writeType = writeType;
		}

		public String getMapFileName() {
			String fileName = interfaceId+"_"+fileId+"_"+this.lengthByte+".xml";
			return fileName;
		}
		
		public String getMapName() {
			String fileName = interfaceId+"_"+fileId+"_"+this.lengthByte;
			return fileName;
		}


		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public FileParserMapConf getMapConf() {
			return mapConf;
		}

		public String getPostMapItemAdaptor() {
			return postMapItemAdaptor;
		}


		public void setPostMapItemAdaptor(String postMapItemAdaptor) {
			this.postMapItemAdaptor = postMapItemAdaptor;
		}


		public String getPostMapListAdaptor() {
			return postMapListAdaptor;
		}


		public void setPostMapListAdaptor(String postMapListAdaptor) {
			this.postMapListAdaptor = postMapListAdaptor;
		}


		public void setMapConf(FileParserMapConf mapConf) {
			this.mapConf = mapConf;
		}


		public void setMapFileName(String mapFileName) {
			this.mapFileName = mapFileName;
		}


	}

}
