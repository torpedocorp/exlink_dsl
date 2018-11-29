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

package kr.co.bizframe.exlink.transform;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.dsl.ExlinkDslEndpoint;
import kr.co.bizframe.exlink.type.GeneratorType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTransform {
	
	public static Logger logger = LoggerFactory.getLogger(AbstractTransform.class);

	public AbstractTransform(ExlinkDslEndpoint endpoint) {
		
	}
	
	public abstract String toString(Object obj);
	
	public abstract Object toObject(String data);
	
	
	public static AbstractTransform getHandler(ExlinkDslEndpoint endpoint){
		AbstractTransform handler = null;
		return handler;
	}
	
	public static AbstractTransform invokeCasePlugIn(GeneratorType type) throws ExlinkDslException {
		AbstractTransform plugIn = null;
		String className = "kr.co.bizframe.camel.dbsync.transform.plugin."+type.getClassName();
		logger.debug("invoke plugin class name=["+className+"]");
		try {
			plugIn = (AbstractTransform) Class.forName(className).newInstance();
		}catch(Exception e) {
			throw new ExlinkDslException("Can not find test case plugin ["+type.getClassName()+"]");
		}
		return plugIn;
	}
	
	
}
