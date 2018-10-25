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
