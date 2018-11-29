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

package kr.co.bizframe.exlink.test.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.bizframe.exlink.transform.AbstractTransform;


public class DummyOrderService implements OrderService {
	public static Logger logger = LoggerFactory.getLogger(DummyOrderService.class);

    // in memory dummy order system
    private Map<Integer, Order> orders = new HashMap<>();

    private final AtomicInteger idGen = new AtomicInteger();

    public DummyOrderService() {
        // setup some dummy orders to start with
        setupDummyOrders();
    }

    @Override
    public Order getOrder(int orderId) {
    	logger.debug("★★★★★★★★★★★★★★★★★★★getOrder★★★★★★★★★★★★★★★★★★★★"+orders.get(orderId));
    	logger.debug("★★★★★★★★★★★★★★★★★★★getOrder★★★★★★★★★★★★★★★★★★★★");
        return orders.get(orderId);
    }

    @Override
    public void updateOrder(Order order) {
    	logger.debug("★★★★★★★★★★★★★★★★★★★updateOrder★★★★★★★★★★★★★★★★★★★★");
        int id = order.getId();
        orders.remove(id);
        orders.put(id, order);
    }

    @Override
    public String createOrder(Order order) {
    	logger.debug("★★★★★★★★★★★★★★★★★★★createOrder★★★★★★★★★★★★★★★★★★★★");
        
        int id = idGen.incrementAndGet();
        order.setId(id);
        orders.put(id, order);
        return "" + id;
    }

    @Override
    public void cancelOrder(int orderId) {
    	logger.debug("★★★★★★★★★★★★★★★★★★★cancelOrder★★★★★★★★★★★★★★★★★★★★");
        
        orders.remove(orderId);
    }

    public void setupDummyOrders() {
    	logger.debug("★★★★★★★★★★★★★★★★★★★setupDummyOrders★★★★★★★★★★★★★★★★★★★★");
        
        Order order = new Order();
        order.setAmount(1);
        order.setPartName("motor");
        order.setCustomerName("honda");
        createOrder(order);

        order = new Order();
        order.setAmount(3);
        order.setPartName("brake");
        order.setCustomerName("toyota");
        createOrder(order);
    }
    
    public String selectCarNo(com.google.gson.internal.LinkedTreeMap param) {
    	logger.debug("★★★★★★★★★★★★★★★★★★★selectCarNo★★★★★★★★★★★★★★★★★★★★"+param);
		 Order order = new Order();
	     order.setAmount(1);
	     order.setPartName("motor");
	     order.setCustomerName("honda");
	     Gson gson = new Gson();
 		return gson.toJson(order);
    }
    
    public String dummy(String test) {
    	logger.debug("★★★★★★★★★★★★★★★★★★★dummy★★★★★★★★★★★★★★★★★★★★"+test);
    	 Order order = new Order();
	     order.setAmount(1);
	     order.setPartName("motor");
	     order.setCustomerName("honda");
	     Gson gson = new Gson();
 		return gson.toJson(order);
    }



}
