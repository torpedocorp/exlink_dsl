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

package kr.co.bizframe.exlink.parser.online.plugin;


import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.online.AbstractOnlineGenerator;
import kr.co.bizframe.exlink.rest.model.RestRequestModel;
import kr.co.bizframe.exlink.sql.SqlFetcher;
import kr.co.bizframe.exlink.sql.scirpt.SqlParser;
import kr.co.bizframe.exlink.type.AppendColumnType;
import kr.co.bizframe.exlink.type.InterfaceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;


public class CaresJsonOnlineGenerator extends AbstractOnlineGenerator{


	public CaresJsonOnlineGenerator() {
	}

	public static Logger logger = LoggerFactory.getLogger(CaresJsonOnlineGenerator.class);
	
	
	public ArrayList<LinkedHashMap<String, Object>> getCarInfoList(String tname, ArrayList<String> param) throws ExlinkDslException{
		SqlParser parser = new SqlParser();
		String runSql = parser.getQuery("select * from " +tname + " where REGCAR_NO IN (:REGCAR_NO) ");
		logger.debug("runSql="+runSql);
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("REGCAR_NO", param);

		SqlFetcher sqlFetcher = new SqlFetcher();
		return sqlFetcher.runWithLowercaseCol(runSql, FileParserConfingFactory.getInstance().getDatasource(), params);
	}
	

	@Override
	public Object generate(Object request) throws ExlinkDslException {
		RestRequestModel req = (RestRequestModel) request;
		ArrayList<String> params = new ArrayList<String>();
		ArrayList dataList = (ArrayList) req.getDataObject();
		for(Object item : dataList){
			AbstractMap map = (AbstractMap)item;
			String regcar_no = (String) map.get("regcar_no");
			logger.debug("item="+item+" regcar_no="+regcar_no);
			params.add(regcar_no);
		}
		InterfaceType type = InterfaceType.CARES;
		FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
		FileParserMapConf mapConf = metaConf.getMapConfByFidAndLength(type.getFileId(), type.getOrder()[0]).getMapConf();
		ArrayList<LinkedHashMap<String, Object>> list = getCarInfoList(mapConf.getTableName(), params);
		return list;
	}


	
	public static void main(String args[]){

		
		try {
			RestRequestModel req = new RestRequestModel();
			CaresJsonOnlineGenerator parser = (CaresJsonOnlineGenerator) AbstractOnlineGenerator.invokeCasePlugIn("CareqJsonOnlineGenerator");
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exlink.xml");
			InterfaceType type = InterfaceType.CAREQ;
			FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
			FileParserMapConf mapConf = metaConf.getMapConfByFidAndLength(type.getFileId(), type.getOrder()[0]).getMapConf();
			logger.debug("req ="+req.toJson());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
