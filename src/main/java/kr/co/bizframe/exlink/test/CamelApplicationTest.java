package kr.co.bizframe.exlink.test;

import org.apache.camel.spring.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelApplicationTest {
	
	
	private static Logger logger = LoggerFactory.getLogger(CamelApplicationTest.class);

	
	public static void main(String args[]) {
		String routeXml = "camel-route-rest.xml";
		logger.debug("load camel config ="+routeXml);
		
		Main main = new Main();
		main.setApplicationContextUri(routeXml);
		try {
			main.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}
