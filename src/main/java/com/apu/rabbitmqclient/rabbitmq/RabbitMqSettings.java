/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqclient.rabbitmq;

/**
 *
 * @author apu
 */
public class RabbitMqSettings {
    
    public static final String TO_SERVER_QUEUE_NAME = "to_server_queue";
    public static final String TO_CLIENT_QUEUE_NAME = "to_client_queue";
    
    public static final String TO_SERVER_EXCHANGE_NAME = "toServerExchange";
    public static final String TO_CLIENT_EXCHANGE_NAME = "toClientExchange";
    
    public static final String ROUTING_KEY = "testRoute";
    
    public static String USERNAME = "guest";
    public static String PASSWORD = "guest";
    public static String VIRTUAL_HOST = "/";
    public static String HOST = "127.0.0.1";
    public static Integer PORT = 5672;
}
