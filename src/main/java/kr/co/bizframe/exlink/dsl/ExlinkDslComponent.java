/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kr.co.bizframe.exlink.dsl;

import java.util.Map;

import kr.co.bizframe.exlink.ExlinkConfig;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the component that manages {@link ExlinkDslEndpoint}.
 */
public class ExlinkDslComponent extends DefaultComponent {

	private static Logger logger = LoggerFactory.getLogger(ExlinkDslComponent.class);
	
	 
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {

    	logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ createEndpoint start");
    	logger.debug("remaining="+remaining);
    	logger.debug("parameters="+parameters);
    	
//    	
    	ExlinkDslEndpoint endpoint = new ExlinkDslEndpoint(uri, this);
    	endpoint.setWorkType(remaining);
//    	endpoint.setMapConfDir((String)parameters.get(ExLinkOptionType.mapConfDir.getCode()));
//    	endpoint.setGenerateFormat((String)parameters.get(ExLinkOptionType.generateFormat.getCode()));
//    	endpoint.setGenerateDir((String)parameters.get(ExLinkOptionType.generateDir.getCode()));
        setProperties(endpoint, parameters);
        logger.debug("endpoint info ="+endpoint.toString());
        FileParserConfingFactory.getInstance().init(ExlinkConfig.getMapDir(),ExlinkConfig.getDsFilename());
        logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ createEndpoint end");
        return endpoint;
    }

}
