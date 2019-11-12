package com.zsw.test.PC.returning;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_Return {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("122.51.107.145");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        //开启返回监听
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int i, String s, String s1, String s2, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                System.out.println("Return Ask");
                System.out.println(i);
                System.out.println(s);
                System.out.println(s1);
                System.out.println(s2);
                System.out.println(basicProperties);
                System.out.println(new String(bytes));
            }
        });

        String exchangeName = "confirm.exchange";
        String routingKey = "confirm.news";
        String routingKeyError = "return.news";

        String msg = "Hello";

        //设为 true 则接收返回值 ，为 false 则直接删除
//        channel.basicPublish(exchangeName,routingKey,true,null,msg.getBytes());

        channel.basicPublish(exchangeName,routingKeyError,true,null,msg.getBytes());

    }

}
