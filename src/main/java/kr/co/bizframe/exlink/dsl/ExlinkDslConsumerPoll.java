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

import kr.co.bizframe.exlink.ExlinkConfig;
import kr.co.bizframe.exlink.parser.file.AbstractFileGenerator;
import kr.co.bizframe.exlink.type.GeneratorType;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExlinkDslConsumerPoll extends ScheduledPollConsumer {
    private final ExlinkDslEndpoint endpoint;

    private static Logger logger = LoggerFactory.getLogger(ExlinkDslConsumerPoll.class);

    
    public ExlinkDslConsumerPoll(ExlinkDslEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.setDelay(10000);
    }

    @Override
    protected int poll() throws Exception {
    	logger.debug("▶▶▶▶▶ start generate send interface files ");
    	
        Exchange exchange = endpoint.createExchange();
        try {
        	logger.debug("endpoint info ="+endpoint);
        	GeneratorType generatorType = GeneratorType.getTypeByCode(endpoint.getFileFormat());
        	logger.debug("generatorType ="+generatorType);
        	AbstractFileGenerator generator = AbstractFileGenerator.invokeCasePlugIn(generatorType);
        	generator.process(ExlinkConfig.getSendFileDir());
            getProcessor().process(exchange);
            return 1; // number of messages polled
        } finally {
            if (exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
            logger.debug("▶▶▶▶▶   end generate send interface files ");
        }
        
    }
    
}
