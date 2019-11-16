package com.zsw.test.DeadLetterExchange;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.zsw.test.utils.ChannelUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = ChannelUtils.create();

        String msg = "hello";

        AMQP.BasicProperties build = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .expiration("10000")
                .build();

        channel.basicPublish("normal.exchange", "normal.news", build, msg.getBytes());

        //关闭连接
//        channel.close();
//        connection.close();

    }

}
