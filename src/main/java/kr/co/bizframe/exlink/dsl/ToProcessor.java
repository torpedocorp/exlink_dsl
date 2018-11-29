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

package kr.co.bizframe.exlink.dsl;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToProcessor implements Processor {
	
	
	public static Logger logger = LoggerFactory.getLogger(ToProcessor.class);

	    
	  
	private String name;
	
	public ToProcessor(String name){
		this.name = name;
	}
	
	
	@Override
	public void process(Exchange exchange) throws Exception {
		logger.debug("simpleProcessor process.! name=" + name);
		Message in = exchange.getIn();
		logger.debug("in ="+in);
		logger.debug("in.getMessageId() ="+in.getMessageId());
	}	
	
}
