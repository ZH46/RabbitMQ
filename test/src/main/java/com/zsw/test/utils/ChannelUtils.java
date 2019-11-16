package com.zsw.test.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChannelUtils {

    public static com.rabbitmq.client.Channel create() throws IOException, TimeoutException {
        //创建 ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("122.51.107.145");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //创建 Connection
        Connection connection = connectionFactory.newConnection();

        //创建 ChannelUtils
        com.rabbitmq.client.Channel channel = connection.createChannel();

        return channel;
    }

}
