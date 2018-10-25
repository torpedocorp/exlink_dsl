package kr.co.bizframe.exlink.util;

import java.io.File;
import java.io.InputStream;
import java.util.AbstractMap;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf.MapField;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;
import kr.co.bizframe.exlink.parser.online.plugin.InsertOnlineDataFormBytefile;
import kr.co.bizframe.exlink.sql.SqlExecutor;
import kr.co.bizframe.exlink.sql.scirpt.SqlParser;
import kr.co.bizframe.exlink.type.AppendColumnType;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBInsertUtil {
	
	public static Logger logger = LoggerFactory.getLogger(DBInsertUtil.class);



	public static void insertItem(AbstractMap<String, Object> params, FileParserMapConf conf )
			throws ExlinkDslException {
		long startTime  = System.currentTimeMillis();
		String sql_scirpt = "";

		String header = "INSERT INTO "+conf.getTableName() + " (";
		String column = "";
		String values = "";

		for(MapField field : conf.getFields()){
			column = column +(column.equals("")? field.getNameEng():", "+field.getNameEng());
			values= values + (values.equals("")? "#"+field.getNameEng():" ,#"+field.getNameEng());
		}
		if(params.get(AppendColumnType.IF_FILE_NAME.getCode()) != null){
			column = column + ", "+AppendColumnType.IF_FILE_NAME.getCode();
			values= values + " ,#"+AppendColumnType.IF_FILE_NAME.getCode();
		}

		if(params.get(AppendColumnType.IF_SYNC_YN.getCode()) != null){
			column = column + ", "+AppendColumnType.IF_SYNC_YN.getCode();
			values= values + " ,#"+AppendColumnType.IF_SYNC_YN.getCode();
		}
		sql_scirpt = header + column+" ) VALUES ("+values+" )";
		SqlParser parser = new SqlParser();
		String runSql = parser.getQuery(sql_scirpt);
		logger.debug(runSql);
		SqlExecutor sqlExecutor = new SqlExecutor();
		sqlExecutor.executeSql(runSql, parser.getColumnList(), FileParserConfingFactory.getInstance().getDatasource(), params);
		long endTime  = System.currentTimeMillis();

	}

}
