package kr.co.bizframe.exlink.parser.conf;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;

import javax.sql.DataSource;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf.MapField;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;
import kr.co.bizframe.exlink.type.FieldDataType;
import kr.co.bizframe.exlink.type.InterfaceType;
import kr.co.bizframe.exlink.util.SqlScriptRunner;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MakeCreateTableScript {

	public static Logger logger = LoggerFactory.getLogger(MakeCreateTableScript.class);

	
	
	
	public void makeCreateTableScirpt(String scriptDir, String sqlDir, String dsFilename){
//		
		try {
			FileParserConfingFactory.getInstance().init(scriptDir, dsFilename);
			
			for(InterfaceType type : InterfaceType.values()){
				File sqlFile = new File(sqlDir, type.getInterfaceId()+".sql");
				StringBuffer scirptSB = new StringBuffer();
				StringBuffer commnetSB = new StringBuffer();
				scirptSB.append("---------------------------------\n");
				scirptSB.append("--"+type.getDesc() + " 생성 스클립트 \n");
				scirptSB.append("---------------------------------\n");
				FileParserMetaConf conf = FileParserConfingFactory.getInstance().getMetaConf(type);
				
				for(FileParserMapConfInfo map : conf.getMapList()){
					makeMapXmlToSql(map, scirptSB, commnetSB);
				}
				FileUtils.writeStringToFile(sqlFile, scirptSB.toString()+commnetSB.toString());
				
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void makeMapXmlToSql(FileParserMapConfInfo map, StringBuffer scirptSB, StringBuffer commnetSB){
		try {
			logger.debug("map info ="+map.getMapConf());
			String tname = map.getMapConf().getTableName();
			scirptSB.append("\n--"+map.getName() + " 생성 스클립트\n");
			scirptSB.append("DROP TABLE "+tname + " CASCADE CONSTRAINTS;\n");
			scirptSB.append("CREATE TABLE "+tname + " ( \n");

			commnetSB.append("COMMENT ON TABLE "+map.getMapConf().getTableName()+" IS '"+map.getName()+"';\n");
			String pkColumns = "";
			int i = 1;
			for(MapField f : map.getMapConf().getFields()){
				logger.debug(f.getIsPK());
				scirptSB.append("\t"+f.getNameEng()+ " "+FieldDataType.getDBType(f.getType())+"("+(f.getLength()+30)+"), \n");
				if(f.getIsPK() != null && f.getIsPK().equals("Y"))
					pkColumns = pkColumns + " "+f.getNameEng()+", ";
				i++;
				commnetSB.append("COMMENT ON COLUMN "+map.getMapConf().getTableName()+"."+f.getNameEng()+" IS '"+f.getNameKor()+"';\n");
			}
			scirptSB.append(" if_file_name VARCHAR2(100), ");
			scirptSB.append(" if_sync_yn   VARCHAR2(1) ");
			if(!pkColumns.equals("")) scirptSB.append(", CONSTRAINT "+map.getMapConf().getTableName()+"_PK PRIMARY KEY ("+pkColumns.substring(0, pkColumns.lastIndexOf(","))+")");
			scirptSB.append(");\n");
			
			String file_idx = "CREATE INDEX "+tname+"_fileidx ON "+tname+"(if_file_name, if_sync_yn);"; 
			scirptSB.append(file_idx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void runSql(String scriptDir, String dsFilename){
		try{
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(dsFilename);
			DataSource dataSource = (DataSource) context.getBean("exlinkDataSource");
			
			for(InterfaceType type : InterfaceType.values()){
				File sqlFile = new File(scriptDir, type.getInterfaceId()+".sql");
				Connection connection = dataSource.getConnection();
				SqlScriptRunner script = new SqlScriptRunner(connection,true);
				FileReader createTables = new FileReader(sqlFile);
				script.runScript(createTables);
				createTables.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void test() throws ExlinkDslException{
		String scriptDir = "misc/mapScript";
		String dsFilename =  "jdbc_ds_exlink.xml";
		FileParserConfingFactory.getInstance().init(scriptDir, dsFilename);
		StringBuffer scirptSB = new StringBuffer();
		StringBuffer commnetSB = new StringBuffer();
		FileParserMetaConf conf = FileParserConfingFactory.getInstance().getMetaConf(InterfaceType.B07000010);
		FileParserMapConfInfo map  = conf.getMapConfByFileId("000");
		logger.debug("choiced map ="+map);
		makeMapXmlToSql(map, scirptSB, commnetSB);
		
		logger.debug("scirptSB ="+scirptSB.toString());
		logger.debug("commnetSB ="+commnetSB.toString());
	}

	
	public static void main(String args[]){
		try{
			String scriptDir = "misc/mapScript";
			String sqlDir = "misc/mapSql";
			String dsFilename =  "jdbc_ds_exclient.xml";
			MakeCreateTableScript mcts = new MakeCreateTableScript();
			mcts.makeCreateTableScirpt(scriptDir, sqlDir, dsFilename);
			mcts.runSql(sqlDir, dsFilename);
//			mcts.test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
