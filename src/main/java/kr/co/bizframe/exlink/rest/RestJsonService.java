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

public class RestJsonService {
	public static Logger logger = LoggerFactory.getLogger(RestJsonService.class);
	
	
	public RestJsonService() throws ExlinkDslException{
		logger.debug("★★★★★★★★★★★★★★★★★★★RestJsonService init ★★★★★★★★★★★★★★★★★★★★");
		logger.debug("ExlinkConfig.dsFilename:"+ExlinkConfig.getDsFilename());
		logger.debug("ExlinkConfig.mapDir:"+ExlinkConfig.getMapDir());
		FileParserConfingFactory.getInstance().init(ExlinkConfig.getMapDir(), ExlinkConfig.getDsFilename());
	}

	public String serviceCA(String reqMsg) throws ExlinkDslException {
		logger.debug("★★★★★★★★★★★★★★★★★★★serviceCA★★★★★★★★★★★★★★★★★★★★");
		logger.debug("ExlinkConfig.dsFilename:"+ExlinkConfig.getDsFilename());
		logger.debug("ExlinkConfig.mapDir:"+ExlinkConfig.getMapDir());
		logger.debug("request message = " + reqMsg);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		RestRequestModel req = gson.fromJson(reqMsg, RestRequestModel.class);
		InterfaceType type = req.getInterfaceType();
		String resMessage = null;
		if(type == InterfaceType.CAREQ){
			logger.debug("to object = " + req);
			CaresJsonOnlineGenerator parser = (CaresJsonOnlineGenerator) AbstractOnlineGenerator.invokeCasePlugIn("CaresJsonOnlineGenerator");
			Object obj = parser.process(req);
			logger.debug("response="+obj);
			
			RestResponseModel res = new RestResponseModel();
			res.setInterfaceType(InterfaceType.CARES);
			res.setFileId(InterfaceType.CARES.getFileId());
			res.setDataObject(obj);
			resMessage = gson.toJson(res);
			logger.debug("resMessage = " + resMessage);
		}else if(type == InterfaceType.DAYCALREQ){
			logger.debug("to object = " + req);
			AbstractOnlineGenerator parser = AbstractOnlineGenerator.invokeCasePlugIn("DaycalJsonOnlineGenerator");
			Object obj = parser.process(req);
			logger.debug("response="+obj);
			
			RestResponseModel res = new RestResponseModel();
			res.setInterfaceType(InterfaceType.DAYCALRES);
			res.setFileId(InterfaceType.DAYCALRES.getFileId());
			res.setDataObject(obj);
			resMessage = gson.toJson(res);
			logger.debug("resMessage = " + resMessage);
		}
		
		return resMessage;
	}
	
	public String serviceOther(String reqMsg) {
		logger.debug("★★★★★★★★★★★★★★★★★★★serviceOther★★★★★★★★★★★★★★★★★★★★");
		RestRequestModel req = new RestRequestModel();
		req.setInterfaceType(InterfaceType.CAREQ);
		req.setFileId(InterfaceType.CAREQ.getFileId());
		logger.debug("request message = " + req);
		Order order = new Order();
		order.setAmount(1);
		order.setPartName("motor");
		order.setCustomerName("honda");
		Gson gson = new Gson();
		return gson.toJson(order);
	}

}
