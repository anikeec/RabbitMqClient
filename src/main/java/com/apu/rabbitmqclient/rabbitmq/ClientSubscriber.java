/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqclient.rabbitmq;

import com.apu.rabbitmqclient.SymbolCounter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apu
 */
public class ClientSubscriber {
    
    private final String exchangeName;
    private final String routingKey;
    private final String queueName;
    private List<Map<Character, Integer>> mapList = new ArrayList<>();
    
    private ConnectionFactory factory;
    private Connection conn;
    private Channel channel;

    public ClientSubscriber(String exchangeName, String routingKey, String queueName) {
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.queueName = queueName;
    }
    
    public List<Map<Character, Integer>> getResultList() {
        return mapList;
    }
    
    public void run() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setUsername(RabbitMqSettings.USERNAME);
        factory.setPassword(RabbitMqSettings.PASSWORD);
        factory.setVirtualHost(RabbitMqSettings.VIRTUAL_HOST);
        factory.setHost(RabbitMqSettings.HOST);
        factory.setPort(RabbitMqSettings.PORT);
        conn = factory.newConnection();
        Channel channel = conn.createChannel();
        boolean durable = true;
        channel.exchangeDeclare(exchangeName, "direct", durable);
        channel.queueDeclare(queueName, durable, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
             public void handleDelivery(String consumerTag,
                                        Envelope envelope, 
                                        AMQP.BasicProperties properties, 
                                        byte[] body) throws IOException {

                    String message = new String(body, StandardCharsets.UTF_8);
                    
                    ObjectMapper mapper = new ObjectMapper();
                    TypeReference<HashMap<Character, Integer>> typeRef 
                      = new TypeReference<HashMap<Character, Integer>>() {};
                    Map<Character, Integer> map = mapper.readValue(message, typeRef);
                    mapList.add(map);
                    System.out.println("Map received.");

             }
        };
        channel.basicConsume(queueName, true, consumer);
    }
    
    public void close() {
        if(channel != null) { 
            try {
                channel.close();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (TimeoutException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
}
