package kr.co.bizframe.exlink.parser.postporcess;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPostMapListAdaptor {
	
	public static Logger logger = LoggerFactory.getLogger(AbstractPostMapListAdaptor.class);
	
	public abstract void process(List<AbstractMap<String, Object>> datalist, FileParserMapConfInfo conf, String key) throws ExlinkDslException;
	
	
	public static AbstractPostMapListAdaptor invokePostMapListAdaptor(String adaptorName) throws ExlinkDslException {
		AbstractPostMapListAdaptor plugIn = null;
		String className = adaptorName;
//		logger.debug("invoke plugin class name=["+className+"]");
		try {
			plugIn = (AbstractPostMapListAdaptor) Class.forName(className).newInstance();
		}catch(Exception e) {
			throw new ExlinkDslException("Can not find file parser plugin ["+className+"]");
		}
		return plugIn;
	}
	
}
