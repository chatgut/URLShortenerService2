package com.example.urlshortenerservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class Publisher{

    RabbitTemplate rabbitTemplate;

    public Publisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishMessage(String message) {
        //rabbitTemplate.convertAndSend("urlShortenerExchange", "urlShortenerRoutingKey", message);

        rabbitTemplate.convertAndSend("messages", message);
    }
}
