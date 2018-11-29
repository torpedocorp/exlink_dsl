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

package kr.co.bizframe.exlink.parser.postporcess.plugin;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf.MapField;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;
import kr.co.bizframe.exlink.parser.postporcess.AbstractPostMapItemAdaptor;
import kr.co.bizframe.exlink.sql.SqlExecutor;
import kr.co.bizframe.exlink.sql.scirpt.SqlParser;
import kr.co.bizframe.exlink.type.AppendColumnType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLogMapitemAdaptor extends AbstractPostMapItemAdaptor{
	
	public static Logger logger = LoggerFactory.getLogger(DefaultLogMapitemAdaptor.class);
	
	private static HashMap<String ,String> sqlMap = new HashMap<String ,String>();

	public DefaultLogMapitemAdaptor() {
		
	}

	/*@Override
	public void process(LinkedHashMap<String, Object> data,
			FileParserMapConfInfo fMapInfo, String key)
			throws ExlinkDslException {
		// TODO Auto-generated method stub
		
	}*/


	@Override
	public void process(AbstractMap<String, Object> params, FileParserMapConfInfo conf, String key)
			throws ExlinkDslException {
		long startTime  = System.currentTimeMillis();
		String sql_scirpt =  sqlMap.get(conf.getMapConf().getConfId());
		
		if(sql_scirpt == null){
//			logger.debug("no hit generate sql ="+key);

			String header = "INSERT INTO "+conf.getMapConf().getTableName() + " (";
			String column = "";
			String values = "";
			
			for(MapField field : conf.getMapConf().getFields()){
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
			sqlMap.put(key, sql_scirpt);
		}
//		logger.debug("hit get sql from memory ="+key);
		
		SqlParser parser = new SqlParser();
		String runSql = parser.getQuery(sql_scirpt);
		logger.debug(runSql);
		SqlExecutor sqlExecutor = new SqlExecutor();
		sqlExecutor.executeSql(runSql, parser.getColumnList(), FileParserConfingFactory.getInstance().getDatasource(), params);
		long endTime  = System.currentTimeMillis();
//		logger.debug("insert time ="+(endTime-startTime));
		
	}
	
	
	
	
	
}
