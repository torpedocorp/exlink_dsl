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

package kr.co.bizframe.exlink.test;

import java.io.File;

import kr.co.bizframe.exlink.parser.conf.FileParserMapConfing;
import kr.co.bizframe.exlink.parser.conf.FileParserMataConfing;
import kr.co.bizframe.exlink.parser.conf.MakeConfingFromExcel;
import kr.co.bizframe.exlink.parser.conf.MakeCreateTableScript;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;

public class MakeScriptFromExcel {
	
	public static void main(String args[]){
		try {
			String targetDir = "misc/mapScript";
			String sqlDir = "misc/mapSql";
			File mapDir =  new File("misc/mapScript");
			for(File xmlFile : mapDir.listFiles()){
				xmlFile.delete();
			}
			MakeConfingFromExcel excelHandler = new MakeConfingFromExcel();
			excelHandler.makeMapConfXmls( new File("misc/mapExcel"),  mapDir);
			FileParserMataConfing.makeMetaConf(new File("misc/mapScript"));
			
			MakeCreateTableScript mcts = new MakeCreateTableScript();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
