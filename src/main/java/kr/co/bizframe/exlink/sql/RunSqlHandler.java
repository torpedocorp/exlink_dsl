package kr.co.bizframe.exlink.sql;

import java.util.List;
import java.util.Map;

import kr.co.bizframe.exlink.handler.ExlinkHandler;
import kr.co.bizframe.exlink.sql.scirpt.SqlScriptInfo;
import kr.co.bizframe.exlink.sql.scirpt.SqlScriptInfo.Sql;
import kr.co.bizframe.exlink.sql.scirpt.SqlScriptParser;
import kr.co.bizframe.exlink.type.SqlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RunSqlHandler {

	public static Logger logger = LoggerFactory.getLogger(RunSqlHandler.class);
	
	ExlinkHandler dbSyncHadler = null;
	
	SqlFetcher sqlFetcher = new SqlFetcher();
	
	SqlExecutor sqlExecutor = new SqlExecutor();
	
	
	public RunSqlHandler(ExlinkHandler dbSyncHadler){
		this.dbSyncHadler = dbSyncHadler;
	}
	
	public List<Map<String, Object>> runSql() throws Exception{
		SqlScriptParser parser = new SqlScriptParser();
		SqlScriptInfo sqlScriptInfo = parser.parse("");
		
		List<Map<String, Object>> rows = null;
		for(Sql sql : sqlScriptInfo.getSqlList()){
			SqlType sqlType = SqlType.getTypeByCode(sql.getType());
			if(sqlType == SqlType.FETCH){
				logger.debug("FETCH="+sql.getRunSql());
				 rows = sqlFetcher.run(sql, dbSyncHadler.getDatasource());
				 logger.debug("rows="+rows);
			}else if(sqlType == SqlType.EXECUTE){
				logger.debug("FETCH="+sql.getRunSql());
				sqlExecutor.run(sql, dbSyncHadler.getDatasource(), rows);
			}
		}
		return rows;
		
	}
	
	public void runSql(List<Map<String, Object>> rows) throws Exception{
		SqlScriptParser parser = new SqlScriptParser();
		SqlScriptInfo sqlScriptInfo = parser.parse("");
		for(Sql sql : sqlScriptInfo.getSqlList()){
			SqlType sqlType = SqlType.getTypeByCode(sql.getType());
			if(sqlType == SqlType.FETCH){
				logger.debug("FETCH="+sql.getRunSql());
				sqlFetcher.run(sql, dbSyncHadler.getDatasource());
				logger.debug("rows="+rows);
			}else if(sqlType == SqlType.EXECUTE){
				logger.debug("FETCH="+sql.getRunSql());
				sqlExecutor.run(sql, dbSyncHadler.getDatasource(), rows);
			}
		}
		
	}

	

	public static void main(String args[]){
		try {


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}

	}

}
