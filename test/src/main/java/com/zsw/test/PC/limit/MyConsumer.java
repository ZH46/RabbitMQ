package com.zsw.test.PC.limit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class MyConsumer extends DefaultConsumer {

    private Channel channel;

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
        String msg = new String(bytes);
        System.out.println(envelope.getDeliveryTag());
        System.out.println(msg);

        //不执行 消息不能被消费
        //主动回送给 Broker 一个应答，执行下一条消息
        //设为 false 就是一条一条接收消息,为 true 就是批量接收消息
        channel.basicAck(envelope.getDeliveryTag(),false);

        //消息消费失败
        // 设为 true 的话，消息重回消息队列
        //channel.basicNack(envelope.getDeliveryTag(),false,true);

    }
}
