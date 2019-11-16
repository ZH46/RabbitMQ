package com.zsw.spring.config;

import com.zsw.spring.convert.ImageMessageConverter;
import com.zsw.spring.convert.MyConverter;
import com.zsw.spring.convert.PDFMessageConverter;
import com.zsw.spring.delegate.MyDelegate;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("122.51.107.145:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        //自动开启
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("hh.exchange",false,false);
    }

    @Bean
    public Queue queue(){
        return new Queue("hh.queue",false);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with("hh");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //设置监听的队列
        container.setQueueNames("confirm.queue","test.queue","hh.queue");
        //设置消费者
        container.setConcurrentConsumers(1);
        //设置最大消费者数量
        container.setMaxConcurrentConsumers(3);
        //设置未消费消息是否返回队列
        container.setDefaultRequeueRejected(false);
        //设置消息的签收机制(Auto自动签收，Manual手动签收)
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //设置消费者的delivery
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return s + "_" + UUID.randomUUID().toString();
            }
        });

        //设置具体的消息监听
        /**
         * 1、方法一
         *
         *
        container.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                String body = new String(message.getBody());
                System.out.println("监听到消息" + body);
            }
        });
         */

        /**
         * 2、适配器方式
         *
         *
         //使用Adapter监听消息
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MyDelegate());
        //修改默认执行的方法
        adapter.setDefaultListenerMethod("consumeMessage");
        //设置消息类型的转换
        adapter.setMessageConverter(new MyConverter());
        */

        /**
         * 3、适配器方式 ：将 队列 与 方法名称 一一对应
         *
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MyDelegate());
        //指定 队列 所调用的 方法名称
        adapter.addQueueOrTagToMethodName("hh.queue","hhMethod");
        adapter.addQueueOrTagToMethodName("test.queue","hhMethod");
        //设置消息类型的转换
        adapter.setMessageConverter(new MyConverter());
         */

        // 1.1 支持json格式的转换器
        /**
         MessageListenerAdapter adapter = new MessageListenerAdapter(new MyDelegate());
         adapter.setDefaultListenerMethod("consumeMessage");

         Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
         adapter.setMessageConverter(jackson2JsonMessageConverter);

         container.setMessageListener(adapter);
         */



        // 1.2 支持java对象转换
        /**
         MessageListenerAdapter adapter = new MessageListenerAdapter(new MyDelegate());
         adapter.setDefaultListenerMethod("consumeMessage");

         Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();

         DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
         jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);

         adapter.setMessageConverter(jackson2JsonMessageConverter);
         container.setMessageListener(adapter);
         */


        //1.3 支持java对象多映射转换
        /**
         MessageListenerAdapter adapter = new MessageListenerAdapter(new MyDelegate());
         adapter.setDefaultListenerMethod("consumeMessage");
         Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
         DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();

         Map<String, Class<?>> idClassMapping = new HashMap<String, Class<?>>();
         idClassMapping.put("order", com.zsw.spring.entity.Order.class);
         idClassMapping.put("packaged", com.zsw.spring.entity.Packaged.class);

         javaTypeMapper.setIdClassMapping(idClassMapping);

         jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
         adapter.setMessageConverter(jackson2JsonMessageConverter);
         container.setMessageListener(adapter);
         */

        //1.4 ext convert
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MyDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");

        //全局的转换器:
        ContentTypeDelegatingMessageConverter convert = new ContentTypeDelegatingMessageConverter();

        MyConverter textConvert = new MyConverter();
        convert.addDelegate("text", textConvert);
        convert.addDelegate("html/text", textConvert);
        convert.addDelegate("xml/text", textConvert);
        convert.addDelegate("text/plain", textConvert);

        Jackson2JsonMessageConverter jsonConvert = new Jackson2JsonMessageConverter();
        convert.addDelegate("json", jsonConvert);
        convert.addDelegate("application/json", jsonConvert);

        ImageMessageConverter imageConverter = new ImageMessageConverter();
        convert.addDelegate("image/png", imageConverter);
        convert.addDelegate("image", imageConverter);

        PDFMessageConverter pdfConverter = new PDFMessageConverter();
        convert.addDelegate("application/pdf", pdfConverter);

        adapter.setMessageConverter(convert);

        //设置消息的监听
        container.setMessageListener(adapter);

        return container;
    }
}
