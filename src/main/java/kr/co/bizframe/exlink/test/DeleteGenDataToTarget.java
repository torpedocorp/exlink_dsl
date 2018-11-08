package kr.co.bizframe.exlink.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;
import kr.co.bizframe.exlink.parser.file.AbstractFileGenerator;
import kr.co.bizframe.exlink.parser.file.plugin.ByteBlockFileParser;
import kr.co.bizframe.exlink.parser.file.plugin.ByteIndicatorFileParser;
import kr.co.bizframe.exlink.parser.file.plugin.JsonFileGenerator;
import kr.co.bizframe.exlink.sql.SqlExecutor;
import kr.co.bizframe.exlink.type.AppendColumnType;
import kr.co.bizframe.exlink.type.GeneratorType;
import kr.co.bizframe.exlink.type.InterfaceType;

public class DeleteGenDataToTarget {
	
	public static Logger logger = LoggerFactory.getLogger(DeleteGenDataToTarget.class);

	public final static String[] DELFILE_LIST = {
		"I001105279999199_IL09001000_B07000010_010753",
	};
	private void deleteTargetData(String tname, String filename) throws ExlinkDslException{
		String runSql = "delete " +tname + " where "+AppendColumnType.IF_FILE_NAME.getCode()+" = '"+filename+"' ";
		MapSqlParameterSource params = new MapSqlParameterSource();
		SqlExecutor sqlExecutor = new SqlExecutor();
		
		sqlExecutor.executeSql(runSql, FileParserConfingFactory.getInstance().getDatasource(), params);
		logger.debug("runSql="+runSql);
	}
	
	
	public void deleteDataFile(InterfaceType type){
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exclient.xml");
			FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
			for(FileParserMapConfInfo mapInfo : metaConf.getMapList()){
				String tname = mapInfo.getMapConf().getTableName();
				for(String filename : DELFILE_LIST){
					deleteTargetData(tname, filename);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public static void main(String args[]){

		DeleteGenDataToTarget ugdt = new DeleteGenDataToTarget();
//		ugdt.updateAllTableReady();
		ugdt.deleteDataFile(InterfaceType.B07000010);

	
	}

}
