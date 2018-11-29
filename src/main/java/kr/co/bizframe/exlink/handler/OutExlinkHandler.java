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

import java.util.List;
import java.util.Map;

import kr.co.bizframe.exlink.dsl.ExlinkDslEndpoint;
import kr.co.bizframe.exlink.sql.RunSqlHandler;
import kr.co.bizframe.exlink.transform.plugin.JsonTransform;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutExlinkHandler extends ExlinkHandler{

	public OutExlinkHandler(ExlinkDslEndpoint endpoint) {
		super(endpoint);
		// TODO Auto-generated constructor stub
	}


	public static Logger logger = LoggerFactory.getLogger(OutExlinkHandler.class);

	@Override
	public void doService(Exchange exchange) {
		try {
			RunSqlHandler runSqlHandler = new RunSqlHandler(this);
			List<Map<String, Object>> rows = runSqlHandler.runSql();
			JsonTransform transfrom = new JsonTransform();
			String message = transfrom.transform(rows);
			exchange.getIn().setBody(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



}
