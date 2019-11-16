package com.zsw.test.PC.returning;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ReturnListener;
import com.zsw.test.utils.ChannelUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_Return {

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = ChannelUtils.create();

        String exchangeName = "confirm.exchange";
        String routingKey = "confirm.news";
        String routingKeyError = "return.news";

        String msg = "Hello";

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

        //设为 true 则接收返回未被消费的消息进入队列，默认为 false 则直接删除
//        channel.basicPublish(exchangeName,routingKey,true,null,msg.getBytes());

        channel.basicPublish(exchangeName,routingKeyError,true,null,msg.getBytes());

    }

}
