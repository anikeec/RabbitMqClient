/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqclient;

import com.apu.rabbitmqclient.utils.FileUtils;
import com.apu.rabbitmqclient.utils.TextUtils;
import com.apu.rabbitmqclient.utils.MapUtils;
import com.apu.rabbitmqclient.rabbitmq.ClientPublisher;
import com.apu.rabbitmqclient.rabbitmq.ClientSubscriber;
import com.apu.rabbitmqclient.rabbitmq.RabbitMqSettings;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apu
 */
public class Main {
    
    private static int AMOUNT_OF_PEACES = 5;
    
    public static void main(String[] args) throws IOException, TimeoutException {
        
        ClientSubscriber subscriber = 
                new ClientSubscriber(RabbitMqSettings.TO_CLIENT_EXCHANGE_NAME, 
                                    RabbitMqSettings.ROUTING_KEY, 
                                    RabbitMqSettings.TO_CLIENT_QUEUE_NAME);
        subscriber.run();
        
        ClientPublisher publisher = 
                new ClientPublisher(RabbitMqSettings.TO_SERVER_EXCHANGE_NAME, 
                                    RabbitMqSettings.ROUTING_KEY, 
                                    RabbitMqSettings.TO_SERVER_QUEUE_NAME);        

        SymbolCounter sc = new SymbolCounter();
        String text = FileUtils.getTextFromFile("d:/Projects_java/RabbitMqClient/_add/War and peace.txt");
        
        System.out.println("\nTest source text(for ability to compare in the future):");   
        System.out.println(sc.getStringResults(sc.countSymbols(text)));        
        
        Long startTime = new Date().getTime();
        
        List<String> textParts = TextUtils.sliceText(text, AMOUNT_OF_PEACES);
        for(String str:textParts) {
            publisher.publishMessage(str);
            
        }
        
        while(subscriber.getResultList().size() < AMOUNT_OF_PEACES) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        System.out.println("All maps are received.");
        
        MapUtils mapUtils = new MapUtils();
        Map<Character,Integer> resultMap = null;        
        for(Map<Character, Integer> map: subscriber.getResultList()) {
            resultMap = mapUtils.addMap(map);
        }
        String result = sc.getStringResults(resultMap);
        System.out.println("\nResult map:");   
        System.out.println(result);
        
        Long finishTime = new Date().getTime();
        
        System.out.println("Operation time:" + (finishTime - startTime) + " ms.");
        
        System.out.println("All maps are received.");
        
        subscriber.close();
    }
    
}
