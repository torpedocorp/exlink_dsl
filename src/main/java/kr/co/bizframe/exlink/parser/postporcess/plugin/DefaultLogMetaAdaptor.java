package kr.co.bizframe.exlink.parser.postporcess.plugin;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.model.ExFileInfo;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf.MapField;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;
import kr.co.bizframe.exlink.parser.postporcess.AbstractPostMapItemAdaptor;
import kr.co.bizframe.exlink.parser.postporcess.AbstractPostMetaAdaptor;
import kr.co.bizframe.exlink.sql.SqlExecutor;
import kr.co.bizframe.exlink.sql.scirpt.SqlParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLogMetaAdaptor extends AbstractPostMetaAdaptor{
	
	public static Logger logger = LoggerFactory.getLogger(DefaultLogMetaAdaptor.class);
	
	public DefaultLogMetaAdaptor() {
		
	}

	@Override
	public void process(FileParserMetaConf metaConf, AbstractMap metaData, ExFileInfo info) throws ExlinkDslException {
		int totalRowCount = 0;
		for(Object key :metaData.keySet()){ 
			List mapDatas = (List) metaData.get(key);
			FileParserMapConfInfo fMapInfo = metaConf.getMapConfByMapKey((String)key);
			int keyCount = mapDatas.size();
			totalRowCount = totalRowCount + keyCount;
			logger.debug("▶▶▶▶▶▶▶▶▶ key="+key+" key row count=["+keyCount+"]  total row count=["+totalRowCount+"]");
		}
	}
	
}
