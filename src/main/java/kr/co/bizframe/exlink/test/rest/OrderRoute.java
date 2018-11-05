package kr.co.bizframe.exlink.test.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class OrderRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
    	
        // configure rest-dsl
//    	getContext().getProperties().put("CamelJacksonEnableTypeConverter", "true");
//        // allow Jackson json to convert to pojo types also
//        getContext().getProperties().put("CamelJacksonTypeConverterToPojo", "true");
        
        from("restlet:http://0.0.0.0:8080/cars?restletMethods=POSTrestletBinding=#refName").unmarshal("gson").bean("orderService", "selectCarNo");
    
//        restConfiguration()
//           // to use jetty component and run on port 8080
//            .component("jetty").port(8080)
//            // use a smaller thread pool in jetty as we do not have so high demand yet
//            .componentProperty("minThreads", "1")
//            .componentProperty("maxThreads", "8").bindingMode(RestBindingMode.json);
            // to setup jetty to use the security handler
            //.endpointProperty("handlers", "#securityHandler");

        // rest services under the orders context-path
//        rest("/orders")
//            .get("{id}")
//                .to("bean:orderService?method=getOrder(${header.id})")
//            .post()
//                .to("bean:orderService?method=createOrder")
//            .put()
//                .to("bean:orderService?method=updateOrder")
//            .delete("{id}")
//                .to("bean:orderService?method=cancelOrder(${header.id})");
//        rest("/cars")
//        .post()
//            .to("bean:orderService?method=selectCarNo");
    }
}
