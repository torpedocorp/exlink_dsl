package kr.co.bizframe.exlink.parser.conf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.type.InterfaceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class FileParserConfingFactory {

	public static Logger logger = LoggerFactory.getLogger(FileParserConfingFactory.class);

	private static FileParserConfingFactory _factory = null;

	private static DataSource datasource;
	
	private static HashMap<String, FileParserMetaConf> metaConf = null;


	private FileParserConfingFactory(){

	}
	
	public void init(String mapDir, String dsFilename) throws ExlinkDslException{
		 init(mapDir, dsFilename, true); 
	}

	public void init(String mapDir, String dsFilename, boolean isLoadMapConf) throws ExlinkDslException{
		if(dsFilename != null){
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(dsFilename);
	    	datasource = (DataSource) context.getBean("exlinkDataSource");
		}
		
		if(isLoadMapConf){
			if(metaConf == null){
				metaConf = new HashMap<String, FileParserMetaConf>();
				ArrayList<FileParserMetaConf> list = FileParserMataConfing.loadConf(new File(mapDir));		
				for(FileParserMetaConf conf : list){
					metaConf.put(conf.getInterfaceId(), conf);
				}
			
				
			}else{
				logger.info("FileParserConfingFactory is already initalized....");
			}
		}
		
		
	}

	public static FileParserConfingFactory getInstance() throws ExlinkDslException{
		if(_factory == null){
			_factory = new FileParserConfingFactory();
		}
		return _factory;
	}


	public FileParserMetaConf getMetaConf(InterfaceType type) throws ExlinkDslException{
		FileParserMetaConf conf = metaConf.get(type.getInterfaceId());
		if(conf == null)
			throw new ExlinkDslException("Interface meta["+type.getInterfaceId()+"] config dose not exist");
		return conf;
	}
	
	public static DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		FileParserConfingFactory.datasource = datasource;
	}

	public static void main(String args[]){
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript",  "jdbc_ds_exlink.xml");
			FileParserMetaConf conf = FileParserConfingFactory.getInstance().getMetaConf(InterfaceType.B60010010);
			logger.debug("get conf ="+conf+"");

		} catch (ExlinkDslException e) {
			e.printStackTrace();
		}

	}


}
