/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqclient;

import com.apu.rabbitmqclient.rabbitmq.ClientPublisher;
import com.apu.rabbitmqclient.rabbitmq.ClientSubscriber;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author apu
 */
public class Main {
    
    private static final String TO_SERVER_QUEUE_NAME = "to_server_queue";
    private static final String TO_CLIENT_QUEUE_NAME = "to_client_queue";
    
    private static final String TO_SERVER_EXCHANGE_NAME = "toServerExchange";
    private static final String TO_CLIENT_EXCHANGE_NAME = "toClientExchange";
    
    private static final String ROUTING_KEY = "testRoute";
    
    private static int AMOUNT_OF_PEACES = 5;
    
    public static void main(String[] args) throws IOException, TimeoutException {
        
        ClientSubscriber subscriber = new ClientSubscriber(TO_CLIENT_EXCHANGE_NAME, ROUTING_KEY, TO_CLIENT_QUEUE_NAME);
        subscriber.run();
        
        ClientPublisher publisher = new ClientPublisher(TO_SERVER_EXCHANGE_NAME, ROUTING_KEY, TO_SERVER_QUEUE_NAME);        

        FileLoader fl = new FileLoader();
        SymbolCounter sc = new SymbolCounter();
        String text = fl.getText("d:/Projects_java/RabbitMqClient/_add/War and peace.txt");
        
        System.out.println(sc.getStringResults(sc.countSymbols(text)));        
        
        Long startTime = new Date().getTime();
        
        List<String> textParts = TextUtils.sliceText(text, AMOUNT_OF_PEACES);
        for(String str:textParts) {
            publisher.publishMessage(str);
            System.out.println("Sent " + str.length() + " symbols.");
        }
        
        while(subscriber.getResultList().size() < AMOUNT_OF_PEACES) {};
        System.out.println("All maps are received.");
        
        MapUtils mapUtils = new MapUtils();
        Map<Character,Integer> resultMap = null;        
        for(Map<Character, Integer> map: subscriber.getResultList()) {
            resultMap = mapUtils.addMap(map);
        }
        String result = sc.getStringResults(resultMap);
        System.out.println(result);
        
        Long finishTime = new Date().getTime();
        
        System.out.println("Operation time:" + (finishTime - startTime) + " ms.");
        
        System.out.println("All maps are received.");
    }

/*    
    public static void main(String[] args) throws IOException, TimeoutException {
        
        FileLoader fl = new FileLoader();
//        String text = fl.getText("d:/Projects_java/RabbitMqClient/_add/War and peace.txt");
        SymbolCounter sc = new SymbolCounter();
//        String result = sc.getStringResults(sc.countSymbols(text));
//        System.out.println(result);
        
        String text = fl.getText("d:/Projects_java/RabbitMqClient/_add/War and peace.txt");
        List<String> textParts = TextUtils.sliceText(text, 5);
//        MapUtils mapUtils = new MapUtils();
//        Map<Character,Integer> resultMap = null;
//        for(String str:textParts) {
//            resultMap = mapUtils.addMap(sc.countSymbols(str));
//        }
//        String result = sc.getStringResults(resultMap);
//        System.out.println(result);
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            String exchangeName = "toServerExchange";
            String routingKey = "testRoute";            
            
            channel.queueDeclare(TO_SERVER_QUEUE_NAME, true, false, false, null);
            
            for(String str:textParts) {
                channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, str.getBytes());
                System.out.println(" [x] Sent '" + str.length() + "'");
            }
            
        }
    }
 
*/
    
}
