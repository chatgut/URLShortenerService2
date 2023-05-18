//package com.example;
//
//import com.rabbitmq.client.ConnectionFactory;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitCOnfiguarition {
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        // Configure the connection factory using the RabbitMQ connection details
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost("localhost");
//        connectionFactory.setPort(5672);
//        connectionFactory.setUsername("rabbitmq");
//        connectionFactory.setPassword("rabbitmq");
//        return connectionFactory.getRabbitConnectionFactory();
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        // Create a RabbitTemplate using the connection factory
//        RabbitTemplate rabbitTemplate = new RabbitTemplate((org.springframework.amqp.rabbit.connection.ConnectionFactory) connectionFactory);
//        // Use Jackson to convert objects to JSON for the message body
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//
//        //https://blog.devops.dev/asynchronous-communication-made-easy-a-guide-to-inter-microservice-communication-in-spring-boot-e557e5796975
//
//        return rabbitTemplate;
//    }
//}
//
