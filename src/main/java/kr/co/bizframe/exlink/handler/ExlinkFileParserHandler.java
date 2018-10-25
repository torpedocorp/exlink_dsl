package kr.co.bizframe.exlink.handler;

import java.io.File;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.file.AbstractFileParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExlinkFileParserHandler {
	
	public static Logger logger = LoggerFactory.getLogger(ExlinkFileParserHandler.class);

	
	
	
	public static void main(String args[]){
		String className = "kr.co.bizframe.camel.dbsync.handler.DbSyncInHandler";
		logger.debug("invoke plugin class name=["+className+"]");
		try {
			ExlinkFileParserHandler plugIn = (ExlinkFileParserHandler) Class.forName(className).newInstance();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
