/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe exlink project licenses this file to you under the Apache License,     
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:                   
 *                                                                              
 *   http://www.apache.org/licenses/LICENSE-2.0                                 
 *                                                                              
 * Unless required by applicable law or agreed to in writing, software          
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the     
 * License for the specific language governing permissions and limitations      
 * under the License.                                                           
 */ 

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
