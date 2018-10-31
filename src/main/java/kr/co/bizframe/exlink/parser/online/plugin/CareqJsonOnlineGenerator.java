package kr.co.bizframe.exlink.parser.online.plugin;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.online.AbstractOnlineGenerator;
import kr.co.bizframe.exlink.rest.model.RestRequestModel;
import kr.co.bizframe.exlink.sql.SqlFetcher;
import kr.co.bizframe.exlink.sql.scirpt.SqlParser;
import kr.co.bizframe.exlink.type.InterfaceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CareqJsonOnlineGenerator extends AbstractOnlineGenerator{


	public CareqJsonOnlineGenerator() {
	}

	public static Logger logger = LoggerFactory.getLogger(CareqJsonOnlineGenerator.class);
	
	
	public ArrayList<LinkedHashMap<String, Object>> getCareqList(String tname) throws ExlinkDslException{
		SqlParser parser = new SqlParser();
		String runSql = parser.getQuery("select * from " +tname );
		logger.debug("runSql="+runSql);
		SqlFetcher sqlFetcher = new SqlFetcher();
		return sqlFetcher.runWithLowercaseCol(runSql, FileParserConfingFactory.getInstance().getDatasource(), null);
	}
	

	@Override
	public Object generate(Object request) throws ExlinkDslException {
		InterfaceType type = InterfaceType.CAREQ;
		FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
		FileParserMapConf mapConf = metaConf.getMapConfByFidAndLength(type.getFileId(), type.getOrder()[0]).getMapConf();
		ArrayList<LinkedHashMap<String, Object>> list = getCareqList(mapConf.getTableName());
		return list;
	}


	
	public static void main(String args[]){

		
		try {
			RestRequestModel req = new RestRequestModel();
			
			CareqJsonOnlineGenerator parser = (CareqJsonOnlineGenerator) AbstractOnlineGenerator.invokeCasePlugIn("CareqJsonOnlineGenerator");
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exlink.xml");
			InterfaceType type = InterfaceType.CAREQ;
			FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
			FileParserMapConf mapConf = metaConf.getMapConfByFidAndLength(type.getFileId(), type.getOrder()[0]).getMapConf();
			ArrayList<LinkedHashMap<String, Object>> list = parser.getCareqList(mapConf.getTableName());
			logger.debug("list data ="+list);
			req.setInterfaceType(type);
			req.setFileId(metaConf.getFileId());
			req.setDataObject(list);
			logger.debug("req ="+req.toJson());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
