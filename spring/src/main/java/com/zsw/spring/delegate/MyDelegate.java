package com.zsw.spring.delegate;

import com.zsw.spring.entity.Order;
import com.zsw.spring.entity.Packaged;

import java.io.File;
import java.util.Map;

/**
 * 消息委托对象，用于处理消息
 */
public class MyDelegate {

    public void handleMessage(byte[] messageBody){
        System.out.println("默认方法，消息内容" + new String(messageBody));
    }

    public void consumeMessage(byte[] messageBody){
        System.out.println("字节数组方法，消息内容" + new String(messageBody));
    }

    public void consumeMessage(String messageBody){
        System.out.println("string方法，消息内容" + messageBody);
    }

    public void hhMethod(String messageBody){
        System.out.println("hh队列，消息内容" + messageBody);
    }

    public void consumeMessage(Map messageBody) {
        System.err.println("map方法, 消息内容:" + messageBody);
    }

    public void consumeMessage(Order order) {
        System.err.println("order对象, 消息内容, id: " + order.getId() +
                ", name: " + order.getName() +
                ", content: "+ order.getContent());
    }

    public void consumeMessage(Packaged pack) {
        System.err.println("package对象, 消息内容, id: " + pack.getId() +
                ", name: " + pack.getName() +
                ", content: "+ pack.getDescription());
    }

    public void consumeMessage(File file) {
        System.err.println("文件对象 方法, 消息内容:" + file.getName());
    }

}
