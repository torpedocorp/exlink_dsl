package kr.co.bizframe.exlink.test.rest;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sun.media.jfxmedia.logging.Logger;

/**
 * 
 * RestDslTest Class Used for Executing the Application
 *
 */
public class RestDslTest{
	
	public static void main(String[] args) throws Exception {
		
		new ClassPathXmlApplicationContext("classpath:camel-jetty-rest.xml");
//		new ClassPathXmlApplicationContext("classpath:camel-restlet.xml");
		Thread.currentThread().sleep(1000000L);
		
		
	}
}