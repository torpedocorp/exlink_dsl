package kr.co.bizframe.exlink.test.rest;


import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.main.Main;

/**
 * A main class to try to run this example
 */
public class OrderMain {

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        // setup a dummy order service
        main.bind("orderService", new DummyOrderService());
        // create Jetty Basic Auth security handler
        main.bind("securityHandler", JettySecurity.createSecurityHandler());
        main.bind("gson", new GsonDataFormat());

        // add the order route with the Rest services
        main.addRouteBuilder(new OrderRoute());

        System.out.println("***************************************************");
        System.out.println("");
        System.out.println("  Rider Auto Parts REST order service");
        System.out.println("");
        System.out.println("  You can try calling this service using http://localhost:8080/orders/1");
        System.out.println("");
        System.out.println("    and use jack/123 as username/password");
        System.out.println("***************************************************");

        // run the application
        main.run();

    }
}
