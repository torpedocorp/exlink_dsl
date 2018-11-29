/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe mas project licenses this file to you under the Apache License,     
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import kr.co.bizframe.exlink.sql.SqlFetcher;
import kr.co.bizframe.exlink.sql.scirpt.SqlParser;
import kr.co.bizframe.exlink.type.AppendColumnType;
import kr.co.bizframe.exlink.type.GeneratorType;
import kr.co.bizframe.exlink.type.InterfaceType;

public class CountInterfaceDataToTarget {
	
	public static Logger logger = LoggerFactory.getLogger(CountInterfaceDataToTarget.class);

	
	private int count(String tname) throws ExlinkDslException{
		String runSql = "select count(*) cnt from " +tname ;
		MapSqlParameterSource params = new MapSqlParameterSource();
		SqlParser parser = new SqlParser();
		SqlFetcher sqlFetcher = new SqlFetcher();
		ArrayList<LinkedHashMap<String, Object>> ret = sqlFetcher.runWithLowercaseCol(runSql, FileParserConfingFactory.getInstance().getDatasource(), null);
		int count = 0;
		for(LinkedHashMap<String, Object> item :  ret){
			logger.debug("runSql="+runSql + " item="+item);
			count = ((BigDecimal) item.get("cnt")).intValue();
		}
		return count;
		
	}
	
	public void countClientData(InterfaceType type){
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exclient.xml");
			FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
			int totalcount = 0;
			for(FileParserMapConfInfo mapInfo : metaConf.getMapList()){
				String tname = mapInfo.getMapConf().getTableName();
				totalcount = totalcount + count(tname);
			}
			logger.debug("★★★★★★★★ totalcount = "+totalcount);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public void countServerData(InterfaceType type){
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_ostmain.xml");
			FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
			int totalcount = 0;
			String[] tablelist = null;
			if(type == InterfaceType.B07000010){
				tablelist = CountInterfaceTable.TABLELIST199;
			}else if(type == InterfaceType.B60010010){
				tablelist = CountInterfaceTable.TABLELIST401;
			}else if(type == InterfaceType.B60020010){
				tablelist = CountInterfaceTable.TABLELIST402;
			}else if(type == InterfaceType.B60060010){
				tablelist = CountInterfaceTable.TABLELIST410;
			}
			
			for(String tname : tablelist){
				totalcount = totalcount + count(tname);
			}
			logger.debug("★★★★★★★★ totalcount = "+totalcount);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public static void main(String args[]){

		CountInterfaceDataToTarget ugdt = new CountInterfaceDataToTarget();
//		ugdt.updateAllTableReady();
//		ugdt.countClientData(InterfaceType.B07000010);
		ugdt.countServerData(InterfaceType.B07000010);

	
	}

}
