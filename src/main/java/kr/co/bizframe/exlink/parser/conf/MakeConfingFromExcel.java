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
import java.io.FilenameFilter;

import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MakeConfingFromExcel {

	public static Logger logger = LoggerFactory.getLogger(MakeConfingFromExcel.class);

	private DataFormatter formatter = new DataFormatter();


	private void getFieldInfo(FileParserMapConf info, File excelFile) throws Exception{


		XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
		int sheetCnt = workbook.getNumberOfSheets();
//		logger.debug("sheet count="+sheetCnt);
		XSSFSheet sheet = workbook.getSheetAt(0);
		String tableName = "";
		for(int i=1; i<sheet.getLastRowNum()+1; i++){
			kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf.MapField field = info.new MapField();
			Row row = sheet.getRow(i);
			logger.debug("row ="+i + " ="+row.getCell(0)+","+row.getCell(1)+","+row.getCell(2)+","+row.getCell(3)+","+row.getCell(4)+","+row.getCell(5)+"");
			field.setOrder(formatter.formatCellValue(row.getCell(0))+"");
			field.setNameKor(row.getCell(1).getStringCellValue());
			field.setType(row.getCell(2).getStringCellValue());
			field.setNameEng(row.getCell(3).getStringCellValue());
			String length = formatter.formatCellValue(row.getCell(4));
			
			logger.debug("getCell type="+row.getCell(4).getCellType());
			
			if(row.getCell(4).getCellType() == 0)
				field.setLength(Integer.parseInt(formatter.formatCellValue(row.getCell(4))));
			else
				field.setLength(Integer.parseInt(row.getCell(4).getStringCellValue()));
			field.setDesc(row.getCell(5).getStringCellValue());
			if(row.getCell(6) != null){
				String isPK = row.getCell(6).getStringCellValue();
				field.setIsPK(isPK);
			}
			
			logger.debug("field="+field);
			info.addFields(field);
		}
	}

	private void getMetaInfo(FileParserMapConf info, File excelFile)throws Exception{


		XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
		int sheetCnt = workbook.getNumberOfSheets();
		XSSFSheet sheet = workbook.getSheetAt(1);
		String tableName = "";

		for(int i=0; i<sheet.getLastRowNum()+1; i++){
			Row row = sheet.getRow(i);
//				logger.debug("row ="+i + " ="+row.getCell(0)+","+row.getCell(1)+","+row.getCell(2)+","+row.getCell(3));
			if(i == 0){
				info.setName(row.getCell(1).getStringCellValue());
				info.setInterfaceId(row.getCell(3).getStringCellValue());
			}else if(i == 1){
				info.setFileId(formatter.formatCellValue(row.getCell(1))+"");
				info.setLengthByte(formatter.formatCellValue(row.getCell(3))+"");
			}else if(i == 2){
				info.setSourceSystem(row.getCell(1).getStringCellValue());
				info.setTargetSystem(row.getCell(3).getStringCellValue());
			}else if(i == 3){
				info.setJobType(row.getCell(1).getStringCellValue());
				info.setJobCycle(row.getCell(3).getStringCellValue());
			}else if(i == 4){
				if(row.getCell(1) != null){
					info.setTableName(row.getCell(1).getStringCellValue());
				}
				
			}
		}
		if(info.getTableName() == null) info.setTableName(info.getDefaultTableName());
	}


	public FileParserMapConf getInterfaceMapConfInfo(File excelFile) {
		try {
			FileParserMapConf info  = new FileParserMapConf();
			this.getFieldInfo(info, excelFile);
			this.getMetaInfo(info, excelFile);
			logger.info("make excel file success=:"+excelFile.getAbsolutePath());
			return info;
		} catch (Exception e) {
			logger.error("make excel file error=:"+excelFile.getAbsolutePath());
			e.printStackTrace();
		}
		return null; 
	}

	public String getXmlFileName(FileParserMapConf info){
		return info.getInterfaceId()+"_"+info.getFileId()+"_"+info.getLengthByte().replace("Byte", "").trim()+".xml";
	}





	public void makeMapConfXmls(File excelDir, File confDir){
		try {
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File directory, String fileName) {
					return fileName.endsWith(".xlsx");
				}
			};
			for(File excelFile : excelDir.listFiles(filter)){
				FileParserMapConf mapConfInfo =  getInterfaceMapConfInfo(excelFile);
				FileParserMapConfing.generate(mapConfInfo, new File(confDir, this.getXmlFileName(mapConfInfo)));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void makeMapConfXmlFile(File excelFile, File confDir){
		try {
			
			FileParserMapConf mapConfInfo =  getInterfaceMapConfInfo(excelFile);
			FileParserMapConfing.generate(mapConfInfo, new File(confDir, this.getXmlFileName(mapConfInfo)));
		} catch (Exception e) {
			logger.error("excel file=:"+excelFile.getAbsolutePath());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}



	public static void main(String[] args) {
		try {
//			File excelFile = new File("misc/mapExcel", "CA_REQ_12.xlsx");
//			String targetDir = "misc/mapScript";
			MakeConfingFromExcel excelHandler = new MakeConfingFromExcel();
			excelHandler.makeMapConfXmls( new File("misc/mapExcel"),  new File("misc/mapScript"));
//			excelHandler.makeMapConfXmlFile(excelFile,  new File("misc/mapScript") );

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}



}
