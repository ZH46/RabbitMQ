package com.zsw.test.PC.confirm;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.zsw.test.utils.ChannelUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_Confirm {

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = ChannelUtils.create();

        //开启 confirm 确认模式
        channel.confirmSelect();

        for(int i=0;i<5;i++){
            String msg = "hello";
            AMQP.BasicProperties build = new AMQP.BasicProperties().builder().deliveryMode(2).expiration("10000").build();
            channel.basicPublish("confirm.exchange","confirm.news",build,msg.getBytes());
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
