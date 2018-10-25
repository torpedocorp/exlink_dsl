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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import kr.co.bizframe.exlink.handler.MergeInsertHandler;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.file.AbstractFileParser;
import kr.co.bizframe.exlink.type.InterfaceType;
import kr.co.bizframe.exlink.type.ParsingType;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The HelloWorld producer.
 */
public class ExlinkDslProducer extends DefaultProducer {
    
	public static Logger logger = LoggerFactory.getLogger(DefaultProducer.class);

	 
	MergeInsertHandler insertHandler = new MergeInsertHandler();
	
	 ArrayList<String> columnList = new ArrayList<String>();
	 
	String runSql = "";
	
    private ExlinkDslEndpoint endpoint;

    public ExlinkDslProducer(ExlinkDslEndpoint endpoint) throws IOException {
        super(endpoint);
        this.endpoint = endpoint;
       
    }

    public void process(Exchange exchange) throws Exception {
    	logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ ExlinkDslProducer start");
    	logger.debug("■"+endpoint.getEndpointUri());
//    	logger.debug("■"+exchange.getIn().getBody());
//    	logger.debug("■"+exchange.getIn().getHeaders());
    	String filepath = (String) exchange.getIn().getHeaders().get("CamelFileAbsolutePath");
    	logger.debug("■ parsing file="+filepath);
    	File file = new File(filepath);
    	ParsingType pType  = ParsingType.getTypeByCode(endpoint.getFileFormat());
    	AbstractFileParser.invokeCasePlugIn(file, pType).process(file);
    	logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ ExlinkDslProducer end");
    }
    
    public static void main(String args[]) throws Exception{
    	FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exlink.xml");
		FileParserMetaConf conf = FileParserConfingFactory.getInstance().getMetaConf(InterfaceType.B60010010);
    	String filepath =  "misc/doc/ESB_DATA/test/I015708159999410_IL09001000_B60060010_004504";
    	filepath =  "misc/doc/ESB_DATA/test/I015308159999401_IL09001000_B60010010_005249";
    	//filepath =  "misc/doc/ESB_DATA/test/test";
    	//logger.debug("■ parsing file="+filepath);
    	File file = new File(filepath);
//    	AbstractOnlineParser.invokeCasePlugIn(file).process(file);
    	logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ ExlinkDslProducer end");
    }

}
