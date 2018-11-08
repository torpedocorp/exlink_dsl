package kr.co.bizframe.exlink.test;

import kr.co.bizframe.exlink.dsl.ExlinkDslComponent;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbSyncCamelTest {
	private static Logger logger = LoggerFactory.getLogger(DbSyncCamelTest.class);
	
	public void test() {
		try{
			
			CamelContext camelContext = new DefaultCamelContext();
			camelContext.getComponent("exlink", ExlinkDslComponent.class);
			camelContext.addRoutes(new RouteBuilder() {
				public void configure() {
					//from("simple:foo?a=11&b=2323&c=2323")
					from("dbsync:out?script=camel_out.script&datasource=jdbc_ds.xml&transform=json")
					//.process(new CsvProcessor("start"))
					.to("file://out");
//					.to("http://localhost:9080/myService/");
					//.to("file:d:/camel/dest");
					//.to("log:?level=ERROR");
				}
			});
			camelContext.start();
			Thread.sleep(1000000);
			camelContext.stop();	
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] argv){
		DbSyncCamelTest sct = new DbSyncCamelTest();
		sct.test();
		
	}
}
