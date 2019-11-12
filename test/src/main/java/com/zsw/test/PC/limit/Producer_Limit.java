package com.zsw.test.PC.limit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_Limit {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("122.51.107.145");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName = "qos.exchange";
        String routingKey = "qos.news";

        for(int i = 0; i<5 ;i++){
            String msg = "Hello";

            //设为 true 则接收返回值 ，为 false 则直接删除
            channel.basicPublish(exchangeName,routingKey,true,null,msg.getBytes());
        }

    }

}
