package com.zsw.test.PC.limit;

import com.rabbitmq.client.Channel;
import com.zsw.test.utils.ChannelUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = ChannelUtils.create();

        String exchangeName = "qos.exchange";
        String queueName = "qos.queue";
        String routingKey = "qos.#";

        //创建 Exchange 和 Queue 并进行 Binding
        channel.exchangeDeclare(exchangeName,"topic",true);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);

        // 设置消息读取限流
        channel.basicQos(0,1,false);

        //开启限流时通常设 AutoAsk 为 false，需要在 MyConsumer 里开启 basicAsk
        channel.basicConsume(queueName,false,new MyConsumer(channel));

    }

}
