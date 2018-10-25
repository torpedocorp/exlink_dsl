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
