package kr.co.bizframe.exlink.parser.file.plugin;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;
import kr.co.bizframe.exlink.parser.file.AbstractFileGenerator;
import kr.co.bizframe.exlink.sql.SqlExecutor;
import kr.co.bizframe.exlink.sql.SqlFetcher;
import kr.co.bizframe.exlink.sql.scirpt.SqlParser;
import kr.co.bizframe.exlink.test.UpdateGenDataToTarget;
import kr.co.bizframe.exlink.type.AppendColumnType;
import kr.co.bizframe.exlink.type.GeneratorType;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;


public class JsonFileGenerator extends AbstractFileGenerator{


	public JsonFileGenerator() {
	}

	public static Logger logger = LoggerFactory.getLogger(JsonFileGenerator.class);
	
	private final static int MAX_ACTIVE_THREAD = 10;
	
	private static BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(MAX_ACTIVE_THREAD*1000);
	
    private static ThreadPoolExecutor  executorService = new ThreadPoolExecutor (3, MAX_ACTIVE_THREAD, 6000000, TimeUnit.SECONDS, blockingQueue);

	
	private List<Map<String, Object>> getFileList(String tname) throws ExlinkDslException{
		SqlParser parser = new SqlParser();
		String runSql = parser.getQuery("select distinct "+AppendColumnType.IF_FILE_NAME.getCode()+" from " +tname );
		runSql = runSql + " where "+AppendColumnType.IF_SYNC_YN.getCode()+" = 'N' and "+AppendColumnType.IF_FILE_NAME.getCode()+" is not null ";
		SqlFetcher sqlFetcher = new SqlFetcher();
		return sqlFetcher.run(runSql, FileParserConfingFactory.getInstance().getDatasource());
	}
	
	private File makeJsonFile(FileParserMetaConf metaConf, String filename) throws ExlinkDslException{
		File jsonFile = null;
		try {
			jsonFile = new File(this.GENERATE_DIR, filename);
			jsonFile.getParentFile().mkdirs();
			LinkedHashMap<String, ArrayList<LinkedHashMap<String, Object>>> genInfos = new LinkedHashMap<String, ArrayList<LinkedHashMap<String, Object>>>();
			for(FileParserMapConfInfo mapInfo : metaConf.getMapList()){
				String tname = mapInfo.getMapConf().getTableName();
				ArrayList<LinkedHashMap<String, Object>> mapdataList =  selectMapInfoByFilename(tname, filename);
				if(mapdataList.size() > 0){
					genInfos.put(mapInfo.getMapName(), mapdataList);
					logger.debug("$$dbdata "+mapInfo.getMapName()+ " data size="+mapdataList.size());
				}
			}
			long startTime = System.currentTimeMillis();
			Gson gson =  new GsonBuilder().setPrettyPrinting().create();
			String data = gson.toJson(genInfos);

			FileUtils.writeStringToFile(jsonFile, data);
			
			//database status update
			for(String key : genInfos.keySet()){
				FileParserMapConfInfo mapInfo = metaConf.getMapConfByMapKey(key);
				logger.debug("mapinf = "+mapInfo);
				String tname = mapInfo.getMapConf().getTableName();
				updateSyncStatus(tname, filename);
			}
			long endTime = System.currentTimeMillis();
			logger.info("■■■■■■■■■■■■■■■ generate json file time="+(endTime - startTime));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ExlinkDslException("json save file error="+e.toString(), e);
		}
		return jsonFile;
	}
	
	
	public void jsonToObject(File file) throws FileNotFoundException{
		long startTime = System.currentTimeMillis();
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader(file));
		LinkedHashMap<String ,List<Map<String, Object>>> data = gson.fromJson(reader, LinkedHashMap.class);
		long endTime = System.currentTimeMillis();
		logger.info("■■■■■■■■■■■■■■■ file to make object json time ="+(endTime - startTime));
		for(String key : data.keySet()){
			List list = data.get(key);
			 logger.debug("##object "+key+ " data size="+list.size());
		}
	}
	
	private void updateSyncStatus(String tname, String filename) throws ExlinkDslException{
		String runSql = "update " +tname + " set "+AppendColumnType.IF_SYNC_YN.getCode()+" = 'Y' where "+AppendColumnType.IF_FILE_NAME.getCode()+" = :"+AppendColumnType.IF_FILE_NAME.getCode().toLowerCase();
		SqlExecutor sqlExecutor = new SqlExecutor();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(AppendColumnType.IF_FILE_NAME.getCode().toLowerCase(), filename, java.sql.Types.VARCHAR);
		sqlExecutor.executeSql(runSql, FileParserConfingFactory.getInstance().getDatasource(), params);
	}
	
	private ArrayList<LinkedHashMap<String, Object>> selectMapInfoByFilename(String tname, String filename) throws ExlinkDslException{
		SqlParser parser = new SqlParser();
		String runSql = parser.getQuery("select *  from " +tname + " where "+AppendColumnType.IF_FILE_NAME.getCode()+" = :"+AppendColumnType.IF_FILE_NAME.getCode().toLowerCase());
		SqlFetcher sqlFetcher = new SqlFetcher();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(AppendColumnType.IF_FILE_NAME.getCode().toLowerCase(), filename, java.sql.Types.VARCHAR);
		ArrayList<LinkedHashMap<String, Object>> list = sqlFetcher.runWithLowercaseCol(runSql, FileParserConfingFactory.getInstance().getDatasource(), params);
		for(Map<String, Object> row : list){
			row.remove(AppendColumnType.IF_FILE_NAME.getCode().toLowerCase());
			row.remove(AppendColumnType.IF_SYNC_YN.getCode().toLowerCase());
		}
		return list;
	}


	
	@Override
	public File generate(FileParserMetaConf metaConf)
			throws ExlinkDslException {
		FileParserMapConfInfo headconf = metaConf.getHeaderInfo();	
		List<Map<String, Object>> fileLists = getFileList(headconf.getMapConf().getTableName());
		List<Future<Boolean>> executeList = new ArrayList<>();
		for(Map<String, Object> fileInfo : fileLists){
			String filename = (String) fileInfo.get(AppendColumnType.IF_FILE_NAME.getCode());
			if(filename != null && !filename.equals("")){
				
				Callable<Boolean> task = () -> {
					long startTime = System.currentTimeMillis();
					try {
						logger.info("■■■■■■■■■■■■■■■ make json file info ="+metaConf.getInterfaceId()+" of header table name:"+headconf.getMapConf().getTableName());
						logger.debug("gen file name:"+filename);
						File jsonFile = makeJsonFile(metaConf, filename); 
						long endTime = System.currentTimeMillis();
						logger.info("■■■■■■■■■■■■■■■ generate sucess file ="+jsonFile.getAbsolutePath());
						logger.info("■■■■■■■■■■■■■■■ CareqJsonOnlineGenerator generator time ="+(endTime - startTime));
					} catch (ExlinkDslException e) {
						logger.error("make json file error:"+filename);
						e.printStackTrace();
						return Boolean.FALSE;
					}
					return Boolean.TRUE;
				};
				Future<Boolean> future = executorService.submit(task);
				executeList.add(future);
			}
		}
		
		for(Future<Boolean> future : executeList) {
             try {
                 future.get();
                 future.isDone();
             } catch (InterruptedException | ExecutionException e) {
                 e.printStackTrace();
                 throw new ExlinkDslException(e);
             }
         }
		 
		return null;
	}
	

	public static void main(String args[]){

		JsonFileGenerator parser = new JsonFileGenerator();
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exlink.xml");
			UpdateGenDataToTarget update = new UpdateGenDataToTarget();
			update.updateAllTableReady();
			
			AbstractFileGenerator.invokeCasePlugIn(GeneratorType.Json).process("misc/doc/ESB_DATA/test/json");
			parser.jsonToObject(new File("misc/doc/ESB_DATA/test/json/I022608159999199_IL09001000_B07000010_001948"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}











}
