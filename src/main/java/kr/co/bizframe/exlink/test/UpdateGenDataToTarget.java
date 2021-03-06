/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe exlink project licenses this file to you under the Apache License,     
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:                   
 *                                                                              
 *   http://www.apache.org/licenses/LICENSE-2.0                                 
 *                                                                              
 * Unless required by applicable law or agreed to in writing, software          
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the     
 * License for the specific language governing permissions and limitations      
 * under the License.                                                           
 */ 

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

public class UpdateGenDataToTarget {
	
	public static Logger logger = LoggerFactory.getLogger(UpdateGenDataToTarget.class);

	
	private void updateSyncStatus(String tname) throws ExlinkDslException{
		String runSql = "update " +tname + " set "+AppendColumnType.IF_SYNC_YN.getCode()+" = 'N' ";
		MapSqlParameterSource params = new MapSqlParameterSource();
		SqlExecutor sqlExecutor = new SqlExecutor();
		
		sqlExecutor.executeSql(runSql, FileParserConfingFactory.getInstance().getDatasource(), params);
		logger.debug("runSql="+runSql);
	}
	
	public void updateAllTableReady(){
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exclient.xml");
			for(InterfaceType type : InterfaceType.values()){
				FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
				for(FileParserMapConfInfo mapInfo : metaConf.getMapList()){
					String tname = mapInfo.getMapConf().getTableName();
					updateSyncStatus(tname);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public void updateInterfaceTableReady(InterfaceType type){
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exclient.xml");
			FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
			for(FileParserMapConfInfo mapInfo : metaConf.getMapList()){
				String tname = mapInfo.getMapConf().getTableName();
				updateSyncStatus(tname);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public static void main(String args[]){

		UpdateGenDataToTarget ugdt = new UpdateGenDataToTarget();
//		ugdt.updateAllTableReady();
		ugdt.updateInterfaceTableReady(InterfaceType.B60060010);

	
	}

}
