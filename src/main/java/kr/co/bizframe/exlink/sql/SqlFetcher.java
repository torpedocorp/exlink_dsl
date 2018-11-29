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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import kr.co.bizframe.exlink.sql.scirpt.SqlScriptInfo;
import kr.co.bizframe.exlink.sql.scirpt.SqlScriptInfo.Sql;
import kr.co.bizframe.exlink.sql.scirpt.SqlScriptParser;
import kr.co.bizframe.exlink.transform.plugin.JsonTransform;
import kr.co.bizframe.exlink.type.SqlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


public class SqlFetcher {

	public static Logger logger = LoggerFactory.getLogger(SqlFetcher.class);

	public List<Map<String, Object>> run(SqlScriptInfo.Sql sql, DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql.getRunSql());
		return rows;

	}

	public  String transform(List<Map<String, Object>> rows) {
		JsonTransform transfrom = new JsonTransform();
		String message = transfrom.transform(rows);
		return message;
	}

	public List<Map<String, Object>> run(String sql, DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		return rows;

	}
	
	public List<Map<String, Object>> run(String sql, DataSource dataSource,  MapSqlParameterSource params) {
		NamedParameterJdbcTemplate  jdbcTemplate = new NamedParameterJdbcTemplate (dataSource);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);
		return rows;

	}
	
	public ArrayList<LinkedHashMap<String, Object>> runWithLowercaseCol(String sql, DataSource dataSource,  MapSqlParameterSource params) {
		NamedParameterJdbcTemplate  jdbcTemplate = new NamedParameterJdbcTemplate (dataSource);
		SqlRowSet rowset  = jdbcTemplate.queryForRowSet(sql, params);
		ArrayList<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
		while(rowset.next()){
			LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
			for(String columnNames : rowset.getMetaData().getColumnNames()){
				item.put(columnNames.toLowerCase(), rowset.getObject(columnNames));
			}
			list.add(item);
		}
		return list;

	}



	public static void main(String args[]){
		try {
			
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jdbc_ds_camel.xml");

			DataSource dataSource = (DataSource) context.getBean("dataSource");
			
			SqlScriptParser parser = new SqlScriptParser();
			SqlScriptInfo sqlScriptInfo = parser.parse("sql-out-script.xml");
			SqlFetcher fhandler = new SqlFetcher();
			SqlExecutor ehandler = new SqlExecutor();
			List<Map<String, Object>> rows = null;
			for(Sql sql : sqlScriptInfo.getSqlList()){
				SqlType sqlType = SqlType.getTypeByCode(sql.getType());
				if(sqlType == SqlType.FETCH){
					logger.debug("FETCH="+sql.getRunSql());
					 rows = fhandler.run(sql, dataSource);
					 logger.debug("rows="+rows);
				}else if(sqlType == SqlType.EXECUTE){
					logger.debug("FETCH="+sql.getRunSql());
					ehandler.run(sql, dataSource, rows);
				}
			}



		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}

	}

}
