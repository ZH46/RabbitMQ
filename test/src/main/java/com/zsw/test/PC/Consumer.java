package com.zsw.test.PC;

import com.rabbitmq.client.Channel;
import com.zsw.test.utils.ChannelUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = ChannelUtils.create();

        String exchangeName = "confirm.exchange";
        String queueName = "confirm.queue";
        String routingKey = "confirm.#";

        //创建 Exchange 和 Queue 并进行 Binding
        channel.exchangeDeclare(exchangeName,"topic",true);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);

        //创建一个 Consumer
        // AutoAck 为 true 是自动接收消息（限流时通常设为 false）
        channel.basicConsume(queueName,true,new MyConsumer(channel));

    }

}
