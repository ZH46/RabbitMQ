package com.zsw.test.PC;

import com.rabbitmq.client.*;

import java.io.IOException;

public class MyConsumer extends DefaultConsumer {

    public MyConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
            String msg = new String(bytes);
            System.out.println(msg);
    }
}
