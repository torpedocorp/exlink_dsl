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

package kr.co.bizframe.exlink.rest;

import javax.imageio.spi.ServiceRegistry;

import kr.co.bizframe.exlink.ExlinkConfig;
import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.online.AbstractOnlineGenerator;
import kr.co.bizframe.exlink.parser.online.plugin.CaresJsonOnlineGenerator;
import kr.co.bizframe.exlink.rest.model.RestRequestModel;
import kr.co.bizframe.exlink.rest.model.RestResponseModel;
import kr.co.bizframe.exlink.test.rest.Order;
import kr.co.bizframe.exlink.type.InterfaceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RestJsonExlinkService {
	public static Logger logger = LoggerFactory.getLogger(RestJsonExlinkService.class);
	
	
	public RestJsonExlinkService() throws ExlinkDslException{
		logger.debug("★★★★★★★★★★★★★★★★★★★RestJsonService init ★★★★★★★★★★★★★★★★★★★★");
		logger.debug("ExlinkConfig.dsFilename:"+ExlinkConfig.getDsFilename());
		logger.debug("ExlinkConfig.mapDir:"+ExlinkConfig.getMapDir());
		FileParserConfingFactory.getInstance().init(ExlinkConfig.getMapDir(), ExlinkConfig.getDsFilename());
	}

	public String service(String reqMsg) throws ExlinkDslException {
		logger.debug("★★★★★★★★★★★★★★★★★★★serviceCA★★★★★★★★★★★★★★★★★★★★");
		logger.debug("ExlinkConfig.dsFilename:"+ExlinkConfig.getDsFilename());
		logger.debug("ExlinkConfig.mapDir:"+ExlinkConfig.getMapDir());
		logger.debug("request message = " + reqMsg);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		RestRequestModel req = gson.fromJson(reqMsg, RestRequestModel.class);
		String resMessage = null;
		Object obj = null;
		try{
			AbstractOnlineService service =  AbstractOnlineService.invokeCasePlugIn(req.getLoadServiceName());
			obj = service.executeService(req);
		}catch(Exception e){
			e.printStackTrace();
			obj = e.toString();
		}
		RestResponseModel res = new RestResponseModel();
		res.setLoadServiceName(req.getLoadServiceName());
		res.setDataObject(obj);
		resMessage = gson.toJson(res);
		return resMessage;
	}
	
}
