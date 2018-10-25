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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The HelloWorld consumer.
 */
public class ExlinkDslConsumerDefault extends DefaultConsumer {
	
    private final ExlinkDslEndpoint endpoint;

    public static Logger logger = LoggerFactory.getLogger(ExlinkDslConsumerDefault.class);

    
    public ExlinkDslConsumerDefault(ExlinkDslEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    
    @Override
	protected void doStart() throws Exception {
    	logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ comsumer start");
        Exchange exchange = endpoint.createExchange();
        try {
            getProcessor().process(exchange);
        } finally {
            // log exception if an exception occurred and was not handled
            if (exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }
}
