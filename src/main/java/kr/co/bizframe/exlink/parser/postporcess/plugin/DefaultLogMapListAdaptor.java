package kr.co.bizframe.exlink.parser.postporcess.plugin;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf.MapField;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;
import kr.co.bizframe.exlink.parser.postporcess.AbstractPostMapItemAdaptor;
import kr.co.bizframe.exlink.parser.postporcess.AbstractPostMapListAdaptor;
import kr.co.bizframe.exlink.sql.SqlExecutor;
import kr.co.bizframe.exlink.sql.scirpt.SqlParser;
import kr.co.bizframe.exlink.type.AppendColumnType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLogMapListAdaptor extends AbstractPostMapListAdaptor{
	
	public static Logger logger = LoggerFactory.getLogger(DefaultLogMapListAdaptor.class);
	

	public DefaultLogMapListAdaptor() {
		
	}


	@Override
	public void process(List<AbstractMap<String, Object>> datalist, FileParserMapConfInfo conf, String key)
			throws ExlinkDslException {
		//logger.debug("▶▶▶▶▶▶▶▶▶ list size of key =["+datalist.size()+"]");
		
	}

	
	
	
	
	
}
