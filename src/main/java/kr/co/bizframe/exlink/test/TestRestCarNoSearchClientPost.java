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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.online.AbstractOnlineGenerator;
import kr.co.bizframe.exlink.rest.AbstractOnlineService;
import kr.co.bizframe.exlink.rest.model.RestRequestModel;
import kr.co.bizframe.exlink.rest.model.RestResponseModel;
import kr.co.bizframe.exlink.rest.plugin.CareqJsonOnlineService;
import kr.co.bizframe.exlink.type.InterfaceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestRestCarNoSearchClientPost {

	public static Logger logger = LoggerFactory.getLogger(TestRestCarNoSearchClientPost.class);

	public static void main(String[] args) {

		try {

			//URL url = new URL("http://localhost:8081/exlink/ca");
			URL url = new URL("http://192.168.10.207:8881/exlink/rest");
//			URL url = new URL("http://dev.torpedo.co.kr:1524/exlink/other");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			RestRequestModel req = new RestRequestModel();

			CareqJsonOnlineService parser = (CareqJsonOnlineService) AbstractOnlineService.invokeCasePlugIn("CareqJsonOnlineService");
			FileParserConfingFactory.getInstance().init("misc/mapScript", "jdbc_ds_exclient.xml");
			InterfaceType type = InterfaceType.CAREQ;
			FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(type);
			FileParserMapConf mapConf = metaConf.getMapConfByFidAndLength(type.getFileId(), type.getOrder()[0]).getMapConf();
			ArrayList<LinkedHashMap<String, Object>> list = parser.getCareqList(mapConf.getTableName());
			req.setLoadServiceName("CaresJsonOnlineService");
			req.setDataObject(list);
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