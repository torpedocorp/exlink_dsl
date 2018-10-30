package kr.co.bizframe.exlink.parser.file;

import java.io.File;

import java.io.UnsupportedEncodingException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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
import kr.co.bizframe.exlink.parser.postporcess.AbstractPostMapListAdaptor;
import kr.co.bizframe.exlink.parser.postporcess.AbstractPostMetaAdaptor;
import kr.co.bizframe.exlink.type.AppendColumnType;
import kr.co.bizframe.exlink.type.FileParseType;
import kr.co.bizframe.exlink.type.InterfaceType;
import kr.co.bizframe.exlink.type.ParsingType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFileParser {

	public static Logger logger = LoggerFactory.getLogger(AbstractFileParser.class);
	private final static int MAX_ACTIVE_THREAD = 100;
//	private final static ExecutorService executorService = Executors.newFixedThreadPool(MAX_ACTIVE_THREAD);
	private static BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(MAX_ACTIVE_THREAD*5);
    private static ThreadPoolExecutor  executorService = new ThreadPoolExecutor (10, MAX_ACTIVE_THREAD, 60000, TimeUnit.MILLISECONDS, blockingQueue);
	private static int activeCount = 0;

	static{
		executorService.setRejectedExecutionHandler(new RejectedExecutionHandler() {
	            @Override
	            public void rejectedExecution(Runnable r,
	                    ThreadPoolExecutor executor) {
	                try {
	                    Thread.sleep(5);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                executor.execute(r);
	            }
	        });
	}
	public AbstractFileParser() {
	}

	public abstract AbstractMap parse(ExFileInfo info) throws ExlinkDslException;
	

	public byte[] readByte(byte[] data, int pos, int length )
	{
		byte[] transplant = new byte[length];

		System.arraycopy(data, pos, transplant, 0, length);

		return transplant;
	}

	protected void addDataToParseInfo(LinkedHashMap<String ,ArrayList<LinkedHashMap<String, Object>>> parseInfo, LinkedHashMap<String, Object> dataMap, String key){
		ArrayList<LinkedHashMap<String, Object>> value = parseInfo.get(key);
		if(value == null){
			value = new ArrayList<LinkedHashMap<String, Object>>();
			value.add(dataMap);
			parseInfo.put(key, value);
		}else{
			value.add(dataMap);
			parseInfo.put(key, value);
		}

	}
	
	
	protected LinkedHashMap<String, Object> readField(byte[] rowByte, FileParserMapConf mapConf) throws UnsupportedEncodingException, ExlinkDslException{
		int start = 0;
		
		LinkedHashMap<String, Object> dataMap = new LinkedHashMap<String, Object>();
		for(MapField field : mapConf.getFields()){
			int length = field.getLength();
			if(rowByte.length < (start+length)){
				throw new ExlinkDslException(mapConf+" data length"+rowByte.length +" read length="+(start+length));
			}
			byte[] fieldValueByte = readByte(rowByte, start, length);
//			logger.debug(field.getOrder()+ " : "+field.getNameKor()+ " ## "+field.getNameEng()+"="+new String(fieldValueByte, "euc-kr"));
			start = length+start;
			dataMap.put(field.getNameEng(), new String(fieldValueByte, "euc-kr").trim());
		}
		return dataMap;
	}
	
	
	protected void postMapListProcess(FileParserMetaConf metaConf, List<AbstractMap<String, Object>> maplist, String key) throws ExlinkDslException{
		FileParserMapConfInfo fMapInfo = metaConf.getMapConfByMapKey(key);
		String mapAdaptorName = fMapInfo.getPostMapListAdaptor();
		if(mapAdaptorName != null && !mapAdaptorName.equals("")){
			AbstractPostMapListAdaptor mapAdaptor = AbstractPostMapListAdaptor.invokePostMapListAdaptor(mapAdaptorName);
			mapAdaptor.process(maplist, fMapInfo, key);
		}
		
	}
	
	protected void postMapItemProcess(FileParserMetaConf metaConf, AbstractMap<String, Object> mapdata, String key, List<Future<Boolean>> executeItemList) throws ExlinkDslException{
		FileParserMapConfInfo fMapInfo = metaConf.getMapConfByMapKey(key);
		String mapAdaptorName = fMapInfo.getPostMapItemAdaptor();
		
		if(mapAdaptorName != null && !mapAdaptorName.equals("")){
			AbstractPostMapItemAdaptor mapAdaptor = AbstractPostMapItemAdaptor.invokePostMapItemAdaptor(mapAdaptorName);
			
			Callable<Boolean> task = () -> {
				long startTime = System.currentTimeMillis();
				try {
					mapAdaptor.process(mapdata, fMapInfo, key);
				} catch (ExlinkDslException e) {
					logger.error("INSERT TO DB ERROR DATA ="+mapdata+" MAPINFO:"+fMapInfo);
					e.printStackTrace();
					return Boolean.FALSE;
				}finally{
//					logger.debug("INSERT TO DB SUCCESS ="+mapdata);
				}
				long endTime = System.currentTimeMillis();
//				logger.debug("INSERT TO DB takas ="+(endTime -startTime));
				return Boolean.TRUE;
			};
			activeCount = ((ThreadPoolExecutor)executorService).getActiveCount();
			int queuesize = ((ThreadPoolExecutor)executorService).getQueue().size();
			Future<Boolean> future = executorService.submit(task);
			executeItemList.add(future);
			
		}
	}
	
	
	protected void postMetaProcess(FileParserMetaConf metaConf, AbstractMap metaData, ExFileInfo info) throws ExlinkDslException{
		String metaAdaptorName = metaConf.getPostMetaAdaptor();
		if(metaAdaptorName != null && !metaAdaptorName.equals("")){
			AbstractPostMetaAdaptor mataAdaptor = AbstractPostMetaAdaptor.invokePostMapAdaptor(metaAdaptorName);
			mataAdaptor.process(metaConf, metaData, info);
		}
	}
	
	public void process(File file) throws ExlinkDslException{
		long filesize = file.length();
		boolean isWriteComplete = false;
		while(!isWriteComplete){
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long tempsize =  file.length();
			logger.debug("★★★★★★★★★★★★★★★★★★★★★★ check file wrting ★★★★★★★★★★★★★★★"+tempsize);
			if(filesize == tempsize) isWriteComplete = true;
			filesize = tempsize;
		}
		ExFileInfo info = new ExFileInfo();
		String fileName = file.getName();
		long startTime = System.currentTimeMillis();
//		logger.debug("file name length="+fileName.length());
		if(fileName.length() == 44){
			String[] list = fileName.split("_");

			info.setDataFile(file);
			info.setJobCode(list[0].substring(0, 1));
			info.setBrenchCode(list[0].substring(1, 5));
			info.setMmdd(list[0].substring(5, 9));
			info.setHhmm(list[0].substring(9, 13));
			info.setFileId(list[0].substring(13, 16));

			info.setSystemCode(list[1].substring(0, 2));
			info.setSendCode(list[1].substring(2, 6));
			info.setRecvCode(list[1].substring(6, 10));

			info.setInterfaceId(list[2]);
			info.setFileSeq(list[3]);
			logger.info("■■■■■■■■■■■ The file ["+file.getName()+"] start paring "+(double) file.length()/(1024*1024)+" mb");

			FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(InterfaceType.getTypeById(info.getInterfaceId()));
//			<String ,List<AbstractMap<String, Object>>>
			AbstractMap metaData = parse(info);
			long parsingendTime = System.currentTimeMillis();
			logger.info("■■■■■■■■■■■ The file ["+file.getName()+"] parsing time ="+(parsingendTime-startTime));

			/*
			 * meta adaptor execute
			 */
			logger.info("metaConf.getPostMetaAdaptor() ="+metaConf.getPostMetaAdaptor());
			if(metaConf.getPostMetaAdaptor() != null ||  !metaConf.getPostMetaAdaptor().equals("")){
				postMetaProcess(metaConf, metaData, info);
			}
			boolean isException = false;
			int rowcount = 0;
			List<Future<Boolean>> executeMapList = new ArrayList<>();
			
            for(Object key :metaData.keySet()){
                Callable<Boolean> task = () -> {
                        List<AbstractMap<String, Object>> mapDatas = (List<AbstractMap<String, Object>>) metaData.get(key);
                        try{
                        	postMapListProcess(metaConf, mapDatas, (String)key);
                        	return true;
	                  	} catch (ExlinkDslException e) {
	    					logger.error("postMapListProcess error ="+e.toString());
	    					e.printStackTrace();
	    					throw new RuntimeException(e);
	    				}
                };

                activeCount = ((ThreadPoolExecutor)executorService).getActiveCount();
                int queuesize = ((ThreadPoolExecutor)executorService).getQueue().size();
                Future<Boolean> future = executorService.submit(task);
                executeMapList.add(future);
            }
            
            for(Future<Boolean> future : executeMapList) {
                try {
                    future.get();
                    future.isDone();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    throw new ExlinkDslException(e);
                }
            }
            
            
            List<Future<Boolean>> executeItemList = new ArrayList<>();
            for(Object key :metaData.keySet()){
	            FileParserMapConfInfo fMapInfo = metaConf.getMapConfByMapKey((String)key);
	            List<AbstractMap<String, Object>> mapDatas = (List<AbstractMap<String, Object>>) metaData.get(key);
	            for(Object mapObj : mapDatas){
	                AbstractMap<String, Object> mapdata = (AbstractMap<String, Object>) mapObj;
	                mapdata.put(AppendColumnType.IF_FILE_NAME.getCode(), fileName);
	                mapdata.put(AppendColumnType.IF_SYNC_YN.getCode(), "N");
	                ///
	                ///map adaptor execute
	                ///
	                postMapItemProcess(metaConf, mapdata, (String)key, executeItemList);
	                rowcount ++;
	                if(rowcount%1000 == 0 ){
	                    //logger.info("The file ["+file.getName()+"] db inserted data count=["+rowcount+"] " );
	                }
	            }
            }
            
            for(Future<Boolean> future : executeItemList) {
                try {
                    future.get();
                    future.isDone();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    throw new ExlinkDslException(e);
                }
            }

			metaData.clear();
			long dbendTime = System.currentTimeMillis();
			logger.info("■■■■■■■■■■■ The file ["+file.getName()+"] file size=["+(filesize/(1024*1024))+"] data count=["+rowcount+"] db insert time ="+(dbendTime-parsingendTime));
		}else{
			logger.error("■■■■■■■■■■■ The file ["+file.getName()+"] not a interface file");
		}
		long endTime = System.currentTimeMillis();
		logger.debug("■■■■■■■■■■■ "+file.getName()+" processing time"+(endTime -startTime));
		
		
		
	}

	public static AbstractFileParser invokeCasePlugIn(File file, ParsingType pType) throws ExlinkDslException {
		String fileName = file.getName();
		String interfaceID = null;
		if(fileName.length() == 44){
			String[] list = fileName.split("_");
			interfaceID = list[2];
		}else{
			logger.error("The file ["+file.getName()+"] not a interface file");
		}
		
		AbstractFileParser plugIn = null;
		String className = null;
		if(ParsingType.Byte == pType){
			FileParseType type = InterfaceType.getTypeById(interfaceID).getParseType();
			className = "kr.co.bizframe.exlink.parser.file.plugin."+pType.getCode()+type.getCode()+"FileParser";
		}else{
			className = "kr.co.bizframe.exlink.parser.file.plugin."+pType.getCode()+"FileParser";
		}
			
		logger.debug("invoke plugin class name=["+className+"]");
		try {
			plugIn = (AbstractFileParser) Class.forName(className).newInstance();
		}catch(Exception e) {
			throw new ExlinkDslException("Can not find file parser plugin ["+className+"]"+e);
		}
		return plugIn;
	}



}
