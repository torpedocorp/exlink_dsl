package kr.co.bizframe.exlink.handler;

import java.io.File;
import java.util.List;
import java.util.Map;

import kr.co.bizframe.exlink.dsl.ExlinkDslEndpoint;
import kr.co.bizframe.exlink.sql.RunSqlHandler;
import kr.co.bizframe.exlink.transform.plugin.JsonTransform;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InExlinkHandler extends ExlinkHandler{

	public static Logger logger = LoggerFactory.getLogger(InExlinkHandler.class);
	
	MergeInsertHandler insertHandler = new MergeInsertHandler();
	
	
	public InExlinkHandler(ExlinkDslEndpoint endpoint){
		super(endpoint);
	}


	

	@Override
	public void doService(Exchange exchange) {
		try {
			JsonTransform transfrom = new JsonTransform();
			logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ exchange.getIn()"+exchange.getIn().getBody());
			logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ exchange.getIn()"+exchange.getOut().getHeaders());
			logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ exchange.getIn()"+exchange.getOut().getHeaders());
			logger.debug("In msg1:"  + exchange.getIn().getBody(String.class));
			String jsonFilePath = (String) exchange.getIn().getHeader("CamelFileAbsolutePath");
			logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ jsonFilePath"+jsonFilePath);
			List<Map<String, Object>> rows = null;
			if(jsonFilePath != null){
				File jsonFile = new File(jsonFilePath);
				rows = transfrom.loadFile(jsonFile.getAbsolutePath());
			}else{
				rows = transfrom.load(exchange.getIn().getBody().toString());
			}
			RunSqlHandler runSqlHandler = new RunSqlHandler(this);
			runSqlHandler.runSql(rows);
			
			logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ rows"+rows);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	



	public static void main(String args[]){
		
	}



}
