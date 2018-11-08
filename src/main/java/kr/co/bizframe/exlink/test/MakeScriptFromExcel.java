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
