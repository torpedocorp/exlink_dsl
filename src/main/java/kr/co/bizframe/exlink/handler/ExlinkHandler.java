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

package kr.co.bizframe.exlink.handler;

import javax.sql.DataSource;

import kr.co.bizframe.exlink.dsl.ExlinkDslEndpoint;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class ExlinkHandler {
	
	public static Logger logger = LoggerFactory.getLogger(ExlinkHandler.class);

	ExlinkDslEndpoint endpoint;
	
	DataSource datasource;
	
	public ExlinkHandler(ExlinkDslEndpoint endpoint) {
		this.endpoint = endpoint;
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
    	datasource = (DataSource) context.getBean("dataSource");
	}
	
	public ExlinkDslEndpoint getEndpoint() {
		return endpoint;
	}

	
	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	public abstract void doService(Exchange exchange);
	
	
	
	
	
	public static void main(String args[]){
		String className = "kr.co.bizframe.camel.dbsync.handler.DbSyncInHandler";
		logger.debug("invoke plugin class name=["+className+"]");
		try {
			ExlinkHandler plugIn = (ExlinkHandler) Class.forName(className).newInstance();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
