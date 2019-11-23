package com.zsw.springboot_consumer.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.zsw.springboot_consumer.entity.Order;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author 卓少武
 * @date 2019/11/17
 */
@Component
public class ReceiveMessage {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-1",durable = "true"),
            exchange = @Exchange(value = "exchange-1",
                    durable = "true",
                    type = "topic",
                    ignoreDeclarationExceptions = "true"),
            key = "111.#"
        )
    )
    @RabbitHandler
    public void receive(Message message, Channel channel) throws IOException {
        System.out.println("------------ Simple Message -----------");
        System.out.println(message.getPayload()+","+message.getHeaders());
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        System.out.println(deliveryTag);
        //手工ACK
        channel.basicAck(deliveryTag,false);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-2",durable = "true"),
            exchange = @Exchange(value = "exchange-2",
                    durable = "true",
                    type = "topic",
                    ignoreDeclarationExceptions = "true"),
            key = "222.#"
        )
    )
    @RabbitHandler
    public void orderReceive(@Payload JSONObject jsonObject,
                             Channel channel,
                             @Headers Map<String,Object> headers) throws IOException {

        System.out.println("------------ Order Message -----------");

        Order order = jsonObject.toJavaObject(Order.class);

        System.out.println("Order ;" + order.getName());

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        System.out.println(deliveryTag);
        //手工逐条 ACK
        channel.basicAck(deliveryTag,false);
    }

}
