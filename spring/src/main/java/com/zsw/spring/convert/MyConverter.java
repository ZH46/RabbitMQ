package com.zsw.spring.convert;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

public class MyConverter implements MessageConverter {

    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        Message message = new Message(o.toString().getBytes(),messageProperties);
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return new String(message.getBody());
    }
}
