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

/**
 *  Copyright (c) 2014 Torpedo Inc..  All rights reserved.
 *
 */
package kr.co.bizframe.exlink.parser.conf;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;

import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf.MapField;
import kr.co.bizframe.exlink.util.XMLUtil;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Young-jun Bae
 * 
 */
public class FileParserMapConfing {

	private static Logger logger = LoggerFactory.getLogger(FileParserMapConfing.class);

	public static Hashtable<String, FileParserMapConf> mapFileInfo = new Hashtable<String, FileParserMapConf>();

	public void loadMapXml(String mapxmlDir) throws Exception{
		File dir = new File(mapxmlDir);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				return fileName.endsWith(".xml");
			}
		};
		for(File file : dir.listFiles(filter)){;
		String fileName = file.getName().replace(".xml", "");
//		logger.debug(fileName);
		FileParserMapConf info = parse(file);
		mapFileInfo.put(fileName, info);
		}
	}

	public static Hashtable<String, FileParserMapConf> getMapFileInfo() {
		return mapFileInfo;
	}


	public static FileParserMapConf parse(String scriptFileName) throws Exception {

		File file;
		FileParserMapConf info = new FileParserMapConf();
		try {
			URL url = FileParserMapConfing.class.getResource("/" + scriptFileName);
//			logger.info("MasConfig file=" + url.toString());
			file = new File(url.getFile());
			info =  parse(file);
		} catch (Exception e) {
			throw e;
		}
		return info;
	}




	public static FileParserMapConf parse(File confFile) throws Exception {
		FileParserMapConf info = new FileParserMapConf();

		Element sqlScriptEle = XMLUtil.getRootElement(confFile);

		// ////////////////////////////////////////////////////////
		// server conf
		// ///////////////////////////////////////////////////////

		Element fileIdEle = sqlScriptEle.getChild("file-id");
		Element nameEle = sqlScriptEle.getChild("name");
		Element interfaceIdEle = sqlScriptEle.getChild("interface-id");
		Element lengthByteEle = sqlScriptEle.getChild("length-byte");
		Element sourceSystemEle = sqlScriptEle.getChild("source-system");
		Element targetSystemEle = sqlScriptEle.getChild("target-system");
		Element jobTypeEle = sqlScriptEle.getChild("job-type");
		Element jobCycleEle = sqlScriptEle.getChild("job-cycle");
		Element tableName = sqlScriptEle.getChild("table-name"); 


		info.setFileId(fileIdEle.getValue());
		info.setName(nameEle.getValue());
		info.setInterfaceId(interfaceIdEle.getValue());
		info.setLengthByte(lengthByteEle.getValue());
		info.setSourceSystem(sourceSystemEle.getValue());
		info.setTargetSystem(targetSystemEle.getValue());
		info.setJobType(jobTypeEle.getValue());
		info.setJobCycle(jobCycleEle.getValue());
		if(tableName != null) info.setTableName(tableName.getValue());



		List<Element> field_list_ele = sqlScriptEle.getChildren("field");
		for (Element field_ele : field_list_ele) {
			MapField field = info.new MapField();

			String order = null;
			Element orderEle =  field_ele.getChild("order");
			if (orderEle != null) {
				order = orderEle.getValue();
			}
			field.setOrder(order);

			String nameKor = null;
			Element nameKorEle =  field_ele.getChild("name-kor");
			if (nameKorEle != null) {
				nameKor = nameKorEle.getValue();
			}
			field.setNameKor(nameKor);

			String nameEng = null;
			Element nameEngEle =  field_ele.getChild("name-eng");
			if (nameEngEle != null) {
				nameEng = nameEngEle.getValue();
			}
			field.setNameEng(nameEng);

			String type = null;
			Element typeEle =  field_ele.getChild("type");
			if (typeEle != null) {
				type = typeEle.getValue();
			}
			field.setType(type);


			String length = null;
			Element lengthEle =  field_ele.getChild("length");
			if (lengthEle != null) {
				length = lengthEle.getValue();
			}
			field.setLength(Integer.parseInt(length));

			
			String isPK = null;
			Element isPKEle =  field_ele.getChild("primary-key");
			if (isPKEle != null) {
				isPK = isPKEle.getValue();
			}
			field.setIsPK(isPK);
			
			String desc = null;
			Element descEle =  field_ele.getChild("desc");
			if (descEle != null) {
				desc = descEle.getValue();
			}
			field.setDesc(desc);


			info.addFields(field);

		}

//		logger.debug(info+"");

		return info;
	}


	public static void generate(FileParserMapConf info, File file) throws IOException{

		Element rootElement =  new Element("interface-map");

		Element fileId = new Element("file-id");
		fileId.setText(info.getFileId());
		Element name = new Element("name");
		name.setText(info.getName());
		Element interfaceId = new Element("interface-id");
		interfaceId.setText(info.getInterfaceId());
		Element lengthByte = new Element("length-byte");
		lengthByte.setText(info.getLengthByte());
		Element sourceSystem =new Element("source-system");
		sourceSystem.setText(info.getSourceSystem());
		Element targetSystem = new Element("target-system");
		targetSystem.setText(info.getTargetSystem());
		Element jobType = new Element("job-type");
		jobType.setText(info.getJobType());
		Element jobCycle = new Element("job-cycle");
		jobCycle.setText(info.getJobCycle());
		Element tableName = new Element("table-name");
		tableName.setText(info.getTableName());
		rootElement.addContent(fileId);
		rootElement.addContent(name);
		rootElement.addContent(interfaceId);
		rootElement.addContent(lengthByte);
		rootElement.addContent(sourceSystem);
		rootElement.addContent(targetSystem);
		rootElement.addContent(jobType);
		rootElement.addContent(jobCycle);
		rootElement.addContent(tableName);

		for(MapField field : info.getFields()){
			Element fieldEle =  new Element("field");
			Element order =  new Element("order");
			order.setText(field.getOrder());
			Element nameKor =  new Element("name-kor");
			nameKor.setText(field.getNameKor());
			Element nameEng =  new Element("name-eng");
			nameEng.setText(field.getNameEng());
			Element type =  new Element("type");
			type.setText(field.getType());
			Element length =  new Element("length");
			length.setText(field.getLength()+"");
			Element primaryKey =  new Element("primary-key");
			primaryKey.setText(field.getIsPK());
			Element desc =  new Element("desc");
			desc.setText(field.getDesc());
			fieldEle.addContent(order);
			fieldEle.addContent(nameKor);
			fieldEle.addContent(nameEng);
			fieldEle.addContent(type);
			fieldEle.addContent(length);
			fieldEle.addContent(primaryKey);
			fieldEle.addContent(desc);
			rootElement.addContent(fieldEle);
		}

		Document doc = new Document(); 
		doc.setRootElement(rootElement);

		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileWriter(file));
	}

	public static void main(String args[]){
		try {
			FileParserMapConfing parser = new FileParserMapConfing();
			parser.parse(new File("misc/mapScript", "B07000010_102_112.xml"));
//			parser.parse("B60010010_401_96.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
