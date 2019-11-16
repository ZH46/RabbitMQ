package com.zsw.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsw.spring.entity.Order;
import com.zsw.spring.entity.Packaged;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void connection(){
        rabbitAdmin.declareExchange(new DirectExchange("test.exchange",false,false,null));
        rabbitAdmin.declareQueue(new Queue("test.queue",false));

        //不懂为什么这个 binding 用不了
//        rabbitAdmin.declareBinding(new Binding("test.queue", Binding.DestinationType.QUEUE,"text.exchange","test",null));

        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("test.queue",false))
        .to(new DirectExchange("test.exchange",false,false,null))
        .with("test"));

//        rabbitAdmin.deleteExchange("test.exchange");
//        rabbitAdmin.deleteQueue("test.queue");
    }

    @Test
    void sendMessage(){

        //设置消息属性
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("aaa","aaa属性");
        messageProperties.setContentType("application/text");

        Message message = new Message("Hello".getBytes(),messageProperties);

        rabbitTemplate.send("hh.exchange","hh",message);

        //转换并发送消息
        //rabbitTemplate.convertAndSend("test.exchange","test","Hello Convert And Send");

    }

    @Test
    public void testSendJsonMessage() throws Exception {

        Order order = new Order();
        order.setId("001");
        order.setName("消息订单");
        order.setContent("描述信息");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("order 4 json: " + json);

        MessageProperties messageProperties = new MessageProperties();
        //这里注意一定要修改contentType为 application/json
        messageProperties.setContentType("application/json");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("test.exchange", "test", message);
    }

    @Test
    public void testSendJavaMessage() throws Exception {

        Order order = new Order();
        order.setId("001");
        order.setName("订单消息");
        order.setContent("订单描述信息");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("order 4 json: " + json);

        MessageProperties messageProperties = new MessageProperties();
        //这里注意一定要修改contentType为 application/json
        messageProperties.setContentType("application/json");
        //指定 Order 对象的类型的全路径
        messageProperties.getHeaders().put("__TypeId__", "com.zsw.spring.entity.Order");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("test.exchange", "test", message);
    }

    @Test
    public void testSendMappingMessage() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        Order order = new Order();
        order.setId("001");
        order.setName("订单消息");
        order.setContent("订单描述信息");

        String json1 = mapper.writeValueAsString(order);
        System.err.println("order 4 json: " + json1);

        MessageProperties messageProperties1 = new MessageProperties();
        //这里注意一定要修改contentType为 application/json
        messageProperties1.setContentType("application/json");
        //指定 Order 对象的类型的 映射名称
        messageProperties1.getHeaders().put("__TypeId__", "order");
        Message message1 = new Message(json1.getBytes(), messageProperties1);
        rabbitTemplate.send("text.exchange", "test", message1);

        Packaged pack = new Packaged();
        pack.setId("002");
        pack.setName("包裹消息");
        pack.setDescription("包裹描述信息");

        String json2 = mapper.writeValueAsString(pack);
        System.err.println("pack 4 json: " + json2);

        MessageProperties messageProperties2 = new MessageProperties();
        //这里注意一定要修改contentType为 application/json
        messageProperties2.setContentType("application/json");
        //指定 Order 对象的类型的 映射名称
        messageProperties2.getHeaders().put("__TypeId__", "packaged");
        Message message2 = new Message(json2.getBytes(), messageProperties2);
        rabbitTemplate.send("test.exchange", "test", message2);
    }

    @Test
    public void testSendExtConverterMessage() throws Exception {
//			byte[] body = Files.readAllBytes(Paths.get("E:/training/images", "3.jpg"));
//			MessageProperties messageProperties = new MessageProperties();
//			messageProperties.setContentType("image/png");
//			messageProperties.getHeaders().put("extName", "png");
//			Message message = new Message(body, messageProperties);
//			rabbitTemplate.send("", "image_queue", message);

        byte[] body = Files.readAllBytes(Paths.get("E:/training/images", "my.pdf"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/pdf");
        Message message = new Message(body, messageProperties);
        rabbitTemplate.send("", "pdf_queue", message);
    }

}
