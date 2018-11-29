/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe mas project licenses this file to you under the Apache License,     
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

/**
 *  Copyright (c) 2014 Torpedo Inc..  All rights reserved.
 *
 */
package kr.co.bizframe.exlink.sql.scirpt;

import java.io.File;
import java.net.URL;
import java.util.List;

import kr.co.bizframe.exlink.sql.scirpt.SqlScriptInfo.Sql;
import kr.co.bizframe.exlink.util.XMLUtil;

import org.jdom.Attribute;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Young-jun Bae
 * 
 */
public class SqlScriptParser {

	private static Logger logger = LoggerFactory.getLogger(SqlScriptParser.class);

	

	public SqlScriptInfo parse(String scriptFileName) throws Exception {

		logger.debug("Sql script paresing ...");
		File file;
		SqlScriptInfo info = new SqlScriptInfo();
		try {
			URL url = SqlScriptParser.class.getResource("/" + scriptFileName);
			logger.info("MasConfig file=" + url.toString());
			file = new File(url.getFile());
			info =  parse(file);
		} catch (Exception e) {
			throw e;
		}
		return info;
	}

	
	private SqlScriptInfo parse(File confFile) throws Exception {
		SqlScriptInfo info = new SqlScriptInfo();
		try {

			Element sqlScriptEle = XMLUtil.getRootElement(confFile);

			// ////////////////////////////////////////////////////////
			// server conf
			// ///////////////////////////////////////////////////////
			Element serverEle = sqlScriptEle.getChild("datasource");
			logger.debug(serverEle.getValue());
			info.setDatasource(serverEle.getValue());
			List<Element> sql_list_ele = sqlScriptEle.getChildren("sql");
			for (Element sql_ele : sql_list_ele) {
				Sql sql = info.new Sql();
				String type = null;
				Attribute type_att = sql_ele.getAttribute("type");
				if (type_att != null) {
					type = type_att.getValue();
				}
				sql.setType(type);
				
				String id = null;
				Attribute id_att = sql_ele.getAttribute("id");
				if (id_att != null) {
					id = id_att.getValue();
				}
				sql.setId(id);

				
				String transform = null;
				Attribute transform_att = sql_ele.getAttribute("transform");
				if (transform_att != null) {
					transform = transform_att.getValue();
				}
				sql.setTransfrom(transform);

				
				String from = null;
				Attribute from_att = sql_ele.getAttribute("from");
				if (from_att != null) {
					from = from_att.getValue();
				}
				sql.setTransfrom(from);
				
				
				String sql_scirpt = sql_ele.getValue();
				sql.setScirpSql(sql_scirpt);

				SqlParser parser = new SqlParser();
				String runSql = parser.getQuery(sql_scirpt);
				sql.setRunSql(runSql);
				sql.setColumnList(parser.getColumnList());
				info.addSqlList(sql);
				
				logger.debug("sql ="+sql);
			}
			
		} catch (Exception e) {
			throw e;
		}
		return info;
	}
	
	public static void main(String args[]){
		try {
			SqlScriptParser parser = new SqlScriptParser();
			parser.parse("sql-out-script.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
