package com.example.urlshortenerservice;

import com.rabbitmq.client.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class UrlShortenerServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerServiceApplication.class, args);

//        RabbitConnection rabbitConnection = new RabbitConnection();
//        rabbitConnection.startConsuming();

    }

}

