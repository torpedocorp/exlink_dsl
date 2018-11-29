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

package kr.co.bizframe.exlink.sql.scirpt;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlParser {
	
	public static Logger logger = LoggerFactory.getLogger(SqlParser.class);
	
	public ArrayList<String> columnList = new ArrayList<String>();
	

	public String getQuery(String sql){
		if(sql.indexOf("#") > -1){
			String head = sql.substring(0, sql.indexOf("#"));
			String tail = sql.substring(sql.indexOf("#"), sql.length());
			String temp = tail.substring(0, tail.indexOf(" "));
			String column = temp.replace("#", "");
			columnList.add(column);
			String append = tail.substring(tail.indexOf(" "), tail.length());
			sql = head + " ? "+ append;
			sql = getQuery(sql);
		}
		return sql;
		
	}

	public ArrayList<String> getColumnList() {
		return columnList;
	}

	
	public static void main(String args[]) throws Exception{
			    
	}
}
