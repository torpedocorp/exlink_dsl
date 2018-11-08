package kr.co.bizframe.exlink.test;

import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MakeInsertSqlTest {
	
	public static Logger logger = LoggerFactory.getLogger(MakeInsertSqlTest.class);


	public static void main(String args[]){
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	

}
