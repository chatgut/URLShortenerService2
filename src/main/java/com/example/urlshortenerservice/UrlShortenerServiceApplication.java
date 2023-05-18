package com.example.urlshortenerservice;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UrlShortenerServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerServiceApplication.class, args);

        RabbitConnection rabbitConnection = new RabbitConnection();
        rabbitConnection.startConsuming();


    }

}

