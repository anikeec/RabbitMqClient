/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqclient.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author apu
 */
public class ClientPublisher {
    
    private final String exchangeName;
    private final String routingKey;
    private final String queueName;

    public ClientPublisher(String exchangeName, String routingKey, String queueName) {
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.queueName = queueName;
    }
    
    public void publishMessage(String message) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(RabbitMqSettings.USERNAME);
        factory.setPassword(RabbitMqSettings.PASSWORD);
        factory.setVirtualHost(RabbitMqSettings.VIRTUAL_HOST);
        factory.setHost(RabbitMqSettings.HOST);
        factory.setPort(RabbitMqSettings.PORT);
        try (Connection connection = factory.newConnection(); 
                Channel channel = connection.createChannel()) {
                        
            channel.queueDeclare(queueName, true, false, false, null);            
            channel.basicPublish(exchangeName, 
                                routingKey, 
                                MessageProperties.PERSISTENT_TEXT_PLAIN, 
                                message.getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent " + message.length() + " symbols.");
            
        }
    }
    
}
