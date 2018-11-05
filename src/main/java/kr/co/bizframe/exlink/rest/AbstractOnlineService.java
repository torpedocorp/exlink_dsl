package kr.co.bizframe.exlink.rest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.model.ExFileInfo;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf.MapField;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;
import kr.co.bizframe.exlink.parser.postporcess.AbstractPostMapItemAdaptor;
import kr.co.bizframe.exlink.parser.postporcess.AbstractPostMetaAdaptor;
import kr.co.bizframe.exlink.rest.model.RestRequestModel;
import kr.co.bizframe.exlink.test.DemoExecutor.DemoTask;
import kr.co.bizframe.exlink.type.AppendColumnType;
import kr.co.bizframe.exlink.type.FileParseType;
import kr.co.bizframe.exlink.type.GeneratorType;
import kr.co.bizframe.exlink.type.InterfaceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractOnlineService {

	public static Logger logger = LoggerFactory.getLogger(AbstractOnlineService.class);
	
	public static String GENERATE_DIR = null;

	public AbstractOnlineService() {
	}

	public abstract Object execute(Object request) throws ExlinkDslException;
	

	public Object executeService(Object request) throws ExlinkDslException{
		Object response = execute(request);
		return response;
	}


	public static AbstractOnlineService invokeCasePlugIn(String classname) throws ExlinkDslException {
		AbstractOnlineService plugIn = null;
		String className = "kr.co.bizframe.exlink.rest.plugin."+classname;
		logger.debug("invoke plugin class name=["+className+"]");
		try {
			plugIn = (AbstractOnlineService) Class.forName(className).newInstance();
		}catch(Exception e) {
			throw new ExlinkDslException("Can not find file parser plugin ["+className+"]");
		}
		return plugIn;
	}

	
	


}
