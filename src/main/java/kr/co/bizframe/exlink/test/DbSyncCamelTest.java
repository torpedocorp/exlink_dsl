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

package kr.co.bizframe.exlink.test;

import kr.co.bizframe.exlink.dsl.ExlinkDslComponent;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbSyncCamelTest {
	private static Logger logger = LoggerFactory.getLogger(DbSyncCamelTest.class);
	
	public void test() {
		try{
			
			CamelContext camelContext = new DefaultCamelContext();
			camelContext.getComponent("exlink", ExlinkDslComponent.class);
			camelContext.addRoutes(new RouteBuilder() {
				public void configure() {
					//from("simple:foo?a=11&b=2323&c=2323")
					from("dbsync:out?script=camel_out.script&datasource=jdbc_ds.xml&transform=json")
					//.process(new CsvProcessor("start"))
					.to("file://out");
//					.to("http://localhost:9080/myService/");
					//.to("file:d:/camel/dest");
					//.to("log:?level=ERROR");
				}
			});
			camelContext.start();
			Thread.sleep(1000000);
			camelContext.stop();	
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] argv){
		DbSyncCamelTest sct = new DbSyncCamelTest();
		sct.test();
		
	}
}
