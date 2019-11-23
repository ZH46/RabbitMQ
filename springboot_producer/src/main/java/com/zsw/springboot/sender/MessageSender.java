package com.zsw.springboot.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zsw.springboot.entity.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 卓少武
 * @date 2019/11/17
 */
@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //返回的 ACK 总是为 false( 因为信道连接关闭了 )
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            System.out.println("confirm correlationData :" + correlationData);
            System.out.println("confirm ask :" + b);
            System.out.println("false reason:" + s);
            if(!b){
                System.out.println("异常处理...");
            }
        }
    };

    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int i, String s, String s1, String s2) {
            System.out.println("return message :" + message + ",exchange :" + s1 + ",routingKey :"
                    + s2 + ",replyCode :" + i + "replyText :" + s);
        }
    };

    public void sendMessage(Object message, Map<String,Object> properties){
        MessageHeaders mhs = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message,mhs);

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        CorrelationData correlationData = new CorrelationData();
        //值必须全局唯一
        correlationData.setId("12345");

        rabbitTemplate.convertAndSend("exchange-1","111.news",msg,correlationData);
    }

    public void sendOrder(Order order){

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        CorrelationData correlationData = new CorrelationData();
        //值必须全局唯一
        correlationData.setId("56789");

        JSONObject jsonObj = (JSONObject) JSON.toJSON(order);

        rabbitTemplate.convertAndSend("exchange-2","222.news",jsonObj,correlationData);
    }


}
