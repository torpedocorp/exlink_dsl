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

package kr.co.bizframe.exlink.parser.conf;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;
import kr.co.bizframe.exlink.type.FileParseType;
import kr.co.bizframe.exlink.type.InterfaceType;
import kr.co.bizframe.exlink.util.XMLUtil;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileParserMataConfing {

	public static Logger logger = LoggerFactory.getLogger(FileParserMataConfing.class);


	public static void generate(HashMap<String, FileParserMapConf> infos, InterfaceType type, File file) throws IOException{
		Element rootElement =  new Element("interface-mata");
		Element interfaceId = new Element("interface-id");
		interfaceId.setText(type.getInterfaceId());
		Element fileId = new Element("file-id");
		fileId.setText(type.getFileId());
		Element desc = new Element("name");
		desc.setText(type.getDesc());
		Element readAdaptor = new Element("file-read-adaptor");
		readAdaptor.setText(type.getParseType().getCode());
		Element writeAdaptor = new Element("file-write-adaptor");
		writeAdaptor.setText(type.getParseType().getCode());
		Element postMetaAdaptor =  new Element("post-meta-adaptor");
//		postMetaAdaptor.setText("kr.co.bizframe.exlink.parser.postporcess.plugin.DefaultLogMetaAdaptor");

		rootElement.addContent(fileId);
		rootElement.addContent(desc);
		rootElement.addContent(interfaceId);
		rootElement.addContent(readAdaptor);
		rootElement.addContent(writeAdaptor);
		rootElement.addContent(postMetaAdaptor);
//		logger.debug("infos="+infos);
//		logger.debug("interface type="+type);
		if(type.getParseType() == FileParseType.Block){
			makeBlockParserInfo(rootElement, type, infos);
		}else if(type.getParseType() == FileParseType.Indicator){
			makeIndicatorParserInfo(rootElement, type, infos);
		}

		Document doc = new Document(); 
		doc.setRootElement(rootElement);

		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileWriter(file));
	}

	private static void makeIndicatorParserInfo(Element rootElement, InterfaceType type, HashMap<String, FileParserMapConf> infos){
//		logger.debug("makeIndicatorParserInfo infos:"+infos);
		TreeMap<String, FileParserMapConf> tm = new TreeMap<String, FileParserMapConf>(infos);
		Iterator<String> iteratorKey = tm.keySet( ).iterator( );   //키값 오름차순 정렬(기본)
		int i =1;
		while(iteratorKey.hasNext()) {
			String key = iteratorKey.next();
			genMapConf(rootElement, infos.get(key), i);
			i++;
		}
	}


	private static void makeBlockParserInfo(Element rootElement, InterfaceType type, HashMap<String, FileParserMapConf> infos){
		int i =1;
		for(String orderIndex : type.getOrder()){
			FileParserMapConf info = infos.get(orderIndex);
			genMapConf(rootElement, info, i);
			i++;
		}
	}

	private static void genMapConf(Element rootElement, FileParserMapConf info, int order){
		Element interfaceEle =  new Element("interface-map");
		Element orderEle =  new Element("order");
		orderEle.setText(order+"");
		Element interfaceIdEle =  new Element("file-id");
		interfaceIdEle.setText(info.getFileId());
		Element lengthByte =  new Element("length-byte");
		lengthByte.setText(info.getLengthByte());
		Element name =  new Element("name");
		name.setText(info.getName());
		Element mapFileNameEle =  new Element("map-file-name");
		mapFileNameEle.setText(info.getConfId());
		Element postMapItemAdaptorEle =  new Element("post-map-item-adaptor");
		postMapItemAdaptorEle.setText("kr.co.bizframe.exlink.parser.postporcess.plugin.DefaultWriteDBMapItemAdaptor");
		Element postMapListAdaptorEle =  new Element("post-map-list-adaptor");
//		postMapListAdaptorEle.setText("kr.co.bizframe.exlink.parser.postporcess.plugin.DefaultLogMapListAdaptor");
		
		
		
		interfaceEle.addContent(orderEle);
		interfaceEle.addContent(name);
		interfaceEle.addContent(interfaceIdEle);
		interfaceEle.addContent(lengthByte);
		interfaceEle.addContent(mapFileNameEle);
		interfaceEle.addContent(postMapItemAdaptorEle);
		interfaceEle.addContent(postMapListAdaptorEle);
		rootElement.addContent(interfaceEle);
	}



	public static FileParserMetaConf parse(File confFile) throws Exception {
		FileParserMetaConf info = new FileParserMetaConf();
		Element sqlScriptEle = XMLUtil.getRootElement(confFile);

		Element fileIdEle = sqlScriptEle.getChild("file-id");
		Element nameEle = sqlScriptEle.getChild("name");
		Element interfaceIdEle = sqlScriptEle.getChild("interface-id");
		Element fileReadEle = sqlScriptEle.getChild("file-read-adaptor");
		Element fileWriteEle = sqlScriptEle.getChild("file-write-adaptor");
		Element postMetaAdaptorEle = sqlScriptEle.getChild("post-meta-adaptor");

		info.setFileId(fileIdEle.getValue());
		info.setName(nameEle.getValue());
		info.setInterfaceId(interfaceIdEle.getValue());
		info.setReadType(FileParseType.getTypeByCode(fileReadEle.getValue()));
		info.setWriteType(FileParseType.getTypeByCode(fileWriteEle.getValue()));
		info.setPostMetaAdaptor(postMetaAdaptorEle.getValue());


		List<Element> ifMapEle = sqlScriptEle.getChildren("interface-map");
		for (Element ifEle : ifMapEle) {
			FileParserMapConfInfo ifMapInfo = info.new FileParserMapConfInfo();
			String order = null;
			Element orderEle =  ifEle.getChild("order");
			if (orderEle != null) {
				order = orderEle.getValue();
			}
			ifMapInfo.setOrder(order);
			
			String fId = null;
			Element fIdEle =  ifEle.getChild("file-id");
			if (fIdEle != null) {
				fId = fIdEle.getValue();
			}
			ifMapInfo.setFileId(fId);

			String name = null;
			Element nameCEle =  ifEle.getChild("name");
			if (nameCEle != null) {
				name = nameCEle.getValue();
			}
			ifMapInfo.setName(name);
			
			

			String length = null;
			Element lengthEle =  ifEle.getChild("length-byte");
			if (lengthEle != null) {
				length = lengthEle.getValue();
			}
			ifMapInfo.setLengthByte(length);
			
			String mapFileName = null;
			Element mapFileNameEle =  ifEle.getChild("map-file-name");
			if (mapFileNameEle != null) {
				mapFileName = mapFileNameEle.getValue();
			}
			ifMapInfo.setMapFileName(mapFileName);
			
			

			Element postMapItemAdaptorEle = ifEle.getChild("post-map-item-adaptor");
			
			String postMapItemAdaptor = null;
			if (postMapItemAdaptorEle != null) {
				postMapItemAdaptor = postMapItemAdaptorEle.getValue();
			}
			ifMapInfo.setPostMapItemAdaptor(postMapItemAdaptor);
			
			
			Element postMapListAdaptorEle = ifEle.getChild("post-map-list-adaptor");
			String postMapListAdaptor = null;
			if (postMapListAdaptorEle != null) {
				postMapListAdaptor = postMapListAdaptorEle.getValue();
			}
			ifMapInfo.setPostMapListAdaptor(postMapListAdaptor);
			
			info.addMapList(ifMapInfo);

		}
		return info;
	}

	public static void makeMetaConf(File mapDir) throws ExlinkDslException{
		try {
			FileParserMataConfing parser = new FileParserMataConfing();
			for(InterfaceType type : InterfaceType.values()){
				FilenameFilter filter = new FilenameFilter() {
					public boolean accept(File directory, String fileName) {
						return fileName.startsWith(type.getInterfaceId()) && !fileName.endsWith("meta.xml")&& !fileName.endsWith(".sql");
					}
				};
				HashMap<String, FileParserMapConf> infos = new HashMap<String, FileParserMapConf>();
				for(File mapFile : mapDir.listFiles(filter)){
					logger.debug(type.getDesc()+":"+mapFile.getAbsolutePath());
					FileParserMapConf conf = FileParserMapConfing.parse(mapFile);
					if(FileParseType.Block == type.getParseType())
						infos.put(conf.getLengthByte(), conf);
					else
						infos.put(conf.getFileId(), conf);
				}
				if(infos.size() > 0){
//					logger.debug("infos:::::::"+infos);
					
					FileParserMataConfing.generate(infos, type, new File(mapDir, type.getMetaFileName()));
//					logger.debug(type.getDesc()+":"+infos);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ExlinkDslException("error in make map config", e);
		}
	}


	public static ArrayList<FileParserMetaConf> loadConf(File mapDir) throws ExlinkDslException{
		ArrayList<FileParserMetaConf> list = new ArrayList<FileParserMetaConf>();
		try {

			FileParserMataConfing parser = new FileParserMataConfing();
			for(InterfaceType type : InterfaceType.values()){
				FilenameFilter filter = new FilenameFilter() {
					public boolean accept(File directory, String fileName) {
						return fileName.startsWith(type.getInterfaceId()) && fileName.endsWith("meta.xml");
					}
				};
				for(File metaFile : mapDir.listFiles(filter)){
					logger.debug(type.getDesc()+":"+metaFile.getAbsolutePath());
					FileParserMetaConf metaConf = parse(metaFile);

					for(FileParserMapConfInfo mapInfo : metaConf.getMapList()){
						File mapFile = new File(mapDir, mapInfo.getMapFileName());
						if(!mapFile.exists()) throw new ExlinkDslException("Map file ["+mapFile.getAbsolutePath()+"] dose not exist..");
						FileParserMapConf mapConf = FileParserMapConfing.parse(mapFile);
						mapInfo.setFile(mapFile);
						mapInfo.setMapConf(mapConf);
					}
					list.add(metaConf);
				}

			}
		} catch (Exception e) {
			throw new ExlinkDslException("error in loading map config", e);
		}
		return list;
	}

	public static void main(String args[]){
		//		
		try {
			//			InterfaceMataConfParser.loadConf(new File("misc/mapScript"));

			FileParserMataConfing.makeMetaConf(new File("misc/mapScript"));
		} catch (ExlinkDslException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
