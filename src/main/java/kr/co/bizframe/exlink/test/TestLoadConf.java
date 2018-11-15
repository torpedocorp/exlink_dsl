package kr.co.bizframe.exlink.test;

import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.type.InterfaceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLoadConf {
	
	public static Logger logger = LoggerFactory.getLogger(TestLoadConf.class);

	public static void main(String args[]){
		try {
			FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(InterfaceType.getTypeById(InterfaceType.B60060010.getInterfaceId()));
			logger.debug("metaConf="+metaConf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
