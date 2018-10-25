package kr.co.bizframe.exlink.parser.postporcess;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.model.ExFileInfo;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPostMetaAdaptor {
	
	public static Logger logger = LoggerFactory.getLogger(AbstractPostMetaAdaptor.class);
	
	public abstract void process(FileParserMetaConf metaConf, AbstractMap metaData, ExFileInfo info) throws ExlinkDslException;
	
	
	public static AbstractPostMetaAdaptor invokePostMapAdaptor(String adaptorName) throws ExlinkDslException {
		AbstractPostMetaAdaptor plugIn = null;
		String className = adaptorName;
		try {
			plugIn = (AbstractPostMetaAdaptor) Class.forName(className).newInstance();
		}catch(Exception e) {
			e.printStackTrace();
			throw new ExlinkDslException("Can not find file parser plugin ["+className+"]");
		}
		return plugIn;
	}
	
}
