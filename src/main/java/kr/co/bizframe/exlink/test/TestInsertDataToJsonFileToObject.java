package kr.co.bizframe.exlink.test;

import java.io.File;

import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.file.AbstractFileGenerator;
import kr.co.bizframe.exlink.parser.file.plugin.ByteIndicatorFileParser;
import kr.co.bizframe.exlink.parser.file.plugin.JsonFileGenerator;
import kr.co.bizframe.exlink.type.GeneratorType;

public class TestInsertDataToJsonFileToObject {
	
	
	public static void main(String args[]){

		ByteIndicatorFileParser parser = new ByteIndicatorFileParser();

		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "");
			File dir = new File("misc/doc/ESB_DATA/test");
			File file = new File(dir, "I010108159999199_IL09001000_B07000010_014458");
			parser.process(file);
			
			JsonFileGenerator generator = new JsonFileGenerator();
			FileParserConfingFactory.getInstance().init("misc/mapScript", "");
			AbstractFileGenerator.invokeCasePlugIn(GeneratorType.Json).process("misc/doc/ESB_DATA/test/json");
			generator.jsonToObject(new File("misc/doc/ESB_DATA/test/json"));
		} catch (Exception e) {
			e.printStackTrace();
//			System.exit(-1);
			
		}
	}

}
