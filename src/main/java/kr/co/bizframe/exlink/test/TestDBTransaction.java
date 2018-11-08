package kr.co.bizframe.exlink.test;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;

public class TestDBTransaction {
	
	
	public static void main(String args[]){
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_rollback.xml");
		} catch (ExlinkDslException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
