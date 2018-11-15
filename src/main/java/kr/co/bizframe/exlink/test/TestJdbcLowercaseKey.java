package kr.co.bizframe.exlink.test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.sql.SqlFetcher;
import kr.co.bizframe.exlink.sql.scirpt.SqlParser;
import kr.co.bizframe.exlink.type.AppendColumnType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class TestJdbcLowercaseKey {

	public static Logger logger = LoggerFactory.getLogger(TestJdbcLowercaseKey.class);

	private void selectMapInfoByFilename(String tname, String filename) throws ExlinkDslException{
		SqlParser parser = new SqlParser();
		String runSql = parser.getQuery("select *  from " +tname + " where "+AppendColumnType.IF_FILE_NAME.getCode()+" = :"+AppendColumnType.IF_FILE_NAME.getCode().toLowerCase());
		SqlFetcher sqlFetcher = new SqlFetcher();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(AppendColumnType.IF_FILE_NAME.getCode().toLowerCase(), filename, java.sql.Types.VARCHAR);
		NamedParameterJdbcTemplate  jdbcTemplate = new NamedParameterJdbcTemplate (FileParserConfingFactory.getDatasource());
		List<Map<String, Object>> ttt = jdbcTemplate.queryForList(runSql, params);
//		for(Map<String, Object> row : list){
//			logger.debug("row="+row);
//			row.remove(AppendColumnType.IF_FILE_NAME.getCode());
//			row.remove(AppendColumnType.IF_SYNC_YN.getCode());
//		}
		logger.debug("list size="+ttt.size());
		SqlRowSet rowset  = jdbcTemplate.queryForRowSet(runSql, params);
		ArrayList<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
		while(rowset.next()){
			logger.debug("row="+rowset.getRow());
			LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
			for(String columnNames : rowset.getMetaData().getColumnNames()){
				item.put(columnNames.toLowerCase(), rowset.getObject(columnNames));
			}
			logger.debug("item="+item);
			list.add(item);
		}
//		ArrayList<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
//		for(String columnNames : rowset.getMetaData().getColumnNames()){
//			LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
//			logger.debug("columnNames="+columnNames);
//		}
		
	}

	public static void main(String args[]) throws ExlinkDslException{
		FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exlink.xml");
		TestJdbcLowercaseKey test = new TestJdbcLowercaseKey();
		test.selectMapInfoByFilename("B60060010_410_115", "I026608159999410_IL09001000_B60060010_025720");
	}



}
