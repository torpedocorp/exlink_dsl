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
