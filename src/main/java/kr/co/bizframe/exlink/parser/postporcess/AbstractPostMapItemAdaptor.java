package kr.co.bizframe.exlink.parser.postporcess;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPostMapItemAdaptor {
	
	public static Logger logger = LoggerFactory.getLogger(AbstractPostMapItemAdaptor.class);
	
	public abstract void process(AbstractMap<String, Object> data, FileParserMapConfInfo fMapInfo, String key) throws ExlinkDslException;
	
	
	public static AbstractPostMapItemAdaptor invokePostMapItemAdaptor(String adaptorName) throws ExlinkDslException {
		AbstractPostMapItemAdaptor plugIn = null;
		String className = adaptorName;
//		logger.debug("invoke plugin class name=["+className+"]");
		try {
			plugIn = (AbstractPostMapItemAdaptor) Class.forName(className).newInstance();
		}catch(Exception e) {
			throw new ExlinkDslException("Can not find file parser plugin ["+className+"]");
		}
		return plugIn;
	}
	
}
