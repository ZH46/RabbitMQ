package com.zsw.test.DeadLetterExchange;

import com.rabbitmq.client.Channel;
import com.zsw.test.utils.ChannelUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = ChannelUtils.create();

        String exchangeName = "normal.exchange";
        String queueName = "normal.queue";
        String routingKey = "normal.#";

        //创建 Exchange 和 Queue 并进行 Binding
        channel.exchangeDeclare(exchangeName,"topic",true);

        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","dlx.exchange");

        channel.queueDeclare(queueName,true,false,false,arguments);
        channel.queueBind(queueName,exchangeName,routingKey);

        //创建死信队列(用于接收没被消费的消息)
        channel.exchangeDeclare("dlx.exchange","topic",true,false,null);
        channel.queueDeclare("dlx.queue",true,false,false,null);
        channel.queueBind("dlx.queue","dlx.exchange","#");

        //创建一个 Consumer
        // AutoAck 为 true 是自动接收消息（限流时通常设为 false）
        channel.basicConsume(queueName,true,new MyConsumer(channel));

    }

}
