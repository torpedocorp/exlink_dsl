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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.rest.model.RestRequestModel;
import kr.co.bizframe.exlink.rest.model.RestResponseModel;
import kr.co.bizframe.exlink.sql.SqlFetcher;
import kr.co.bizframe.exlink.sql.scirpt.SqlParser;
import kr.co.bizframe.exlink.type.InterfaceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestOnlineDayCalClientPost {

	public static Logger logger = LoggerFactory.getLogger(TestOnlineDayCalClientPost.class);
	
	
	public static final String TARGET_TB_LIST[] = {"B60010010_401_314", "B60010010_401_115", "B60010010_401_97"};
	
	
	public ArrayList<LinkedHashMap<String, Object>> getCareqList(String tname) throws ExlinkDslException{
		SqlParser parser = new SqlParser();
		String runSql = parser.getQuery("select * from " +tname +" where rownum < 2 ");
		logger.debug("runSql="+runSql);
		SqlFetcher sqlFetcher = new SqlFetcher();
		return sqlFetcher.runWithLowercaseCol(runSql, FileParserConfingFactory.getInstance().getDatasource(), null);
	}
	
	public ArrayList<LinkedHashMap<String, Object>> getTargetList(String tname, String where) throws ExlinkDslException{
		SqlParser parser = new SqlParser();
		String runSql = parser.getQuery("select * from " + tname+ " "+ where );
		logger.debug("runSql="+runSql);
		SqlFetcher sqlFetcher = new SqlFetcher();
		return sqlFetcher.runWithLowercaseCol(runSql, FileParserConfingFactory.getInstance().getDatasource(), null);
	}
	
	
	private void getTargetList() throws ExlinkDslException{
		ArrayList<LinkedHashMap<String, Object>> list = getCareqList("B60010010_401_314");
		logger.debug("target data size="+list.size());
		for(LinkedHashMap<String, Object> item : list){
			logger.debug("item="+item);
			String ic_code = (String) item.get("ic_code");
			String work_date = (String) item.get("work_date");
			String work_no = (String) item.get("work_no");
			String ser_no = (String) item.get("ser_no");
			String where = "";
			where = where + "where ic_code = '"+ic_code+"'";
			where = where + "and work_date = '"+work_date+"'";
			where = where + "and work_no = '"+work_no+"'";
			where = where + "and ser_no = '"+ser_no+"'";
			LinkedHashMap<String, Object> sendData = new LinkedHashMap<String, Object>();
			for(String tname : TARGET_TB_LIST){
				ArrayList<LinkedHashMap<String, Object>> itemList =  getTargetList(tname, where);
				sendData.put(tname, itemList);
				
			}
			logger.debug("target sendData="+sendData);
			try{
				 doRequest(sendData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) {
		
		try {
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exclient.xml", false);
			TestOnlineDayCalClientPost test = new TestOnlineDayCalClientPost();
			test.getTargetList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doRequest(LinkedHashMap<String, Object> sendData) {

		try {

			//URL url = new URL("http://localhost:8081/exlink/ca");
			URL url = new URL("http://dev.torpedo.co.kr:38888/exlink/rest");
//			URL url = new URL("http://dev.torpedo.co.kr:1524/exlink/other");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setConnectTimeout(2000);
			RestRequestModel req = new RestRequestModel();

		
			InterfaceType type = InterfaceType.DAYCALREQ;
			req.setLoadServiceName("DaycalJsonOnlineService");
			req.setDataObject(sendData);
			logger.debug("req ="+req.toJson());

			String input = req.toJson();

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			os.close();
			logger.debug("getResponseCode ="+conn.getResponseCode());

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}
			InputStream is = conn.getInputStream();
			byte[] buff = new byte[8000];

			int bytesRead = 0;

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while((bytesRead = is.read(buff)) != -1) {
				baos.write(buff, 0, bytesRead);
			}
			is.close();
			System.out.println("res json = "+new String(baos.toByteArray()));
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			RestResponseModel res = gson.fromJson(new String(baos.toByteArray()), RestResponseModel.class);
			System.out.println("res = "+res);
			baos.flush();
			baos.close();
			conn.disconnect();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}