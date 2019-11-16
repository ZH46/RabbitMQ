package com.zsw.test.PC.limit;

import com.rabbitmq.client.Channel;
import com.zsw.test.utils.ChannelUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_Limit {

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = ChannelUtils.create();

        String exchangeName = "qos.exchange";
        String routingKey = "qos.news";

        for(int i = 0; i<5 ;i++){
            String msg = "Hello";
            channel.basicPublish(exchangeName,routingKey,null,msg.getBytes());
        }

    }

}
