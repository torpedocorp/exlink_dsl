package kr.co.bizframe.exlink.test.rest;

public interface OrderService {

    Order getOrder(int orderId);

    void updateOrder(Order order);

    String createOrder(Order order);

    void cancelOrder(int orderId);

}
