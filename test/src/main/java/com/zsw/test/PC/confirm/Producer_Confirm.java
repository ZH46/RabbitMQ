package com.zsw.test.PC.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_Confirm {

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

        //开启 confirm 确认模式
        channel.confirmSelect();

        for(int i=0;i<5;i++){
            String msg = "hello";
            channel.basicPublish("confirm.exchange","confirm.news",null,msg.getBytes());
        }

        // 添加一个确认监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("Ask!");
                System.out.println(l);
            }

            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("Not Ask!");
                System.out.println(l);
            }
        });

        //关闭连接
//        channel.close();
//        connection.close();

    }

}
