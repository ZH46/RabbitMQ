package com.zsw.test.PC.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {

        //创建 ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("122.51.107.145");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //创建 Connection
        Connection connection = connectionFactory.newConnection();

        //创建 Channel
        Channel channel = connection.createChannel();

        String exchangeName = "qos.exchange";
        String queueName = "qos.queue";
        String routingKey = "qos.#";

        //创建 Exchange 和 Queue 并进行 Binding
        channel.exchangeDeclare(exchangeName,"topic",true);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);

        // 设置消息读取限流
        channel.basicQos(0,1,false);

        //创建一个 Consumer
        //开启限流时通常设 AutoAsk 为 false，需要在 MyConsumer 里开启 Ask
        channel.basicConsume(queueName,false,new MyConsumer(channel));

    }

}
