package com.example.urlshortenerservice;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class UrlShortenerServiceApplication implements CommandLineRunner {

    @Autowired
    private RabbitConnection rabbitConnection;

    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerServiceApplication.class, args);

        RabbitConnection rabbitConnection = new RabbitConnection();
        rabbitConnection.startConsuming();

    }

    @Override
    public void run(String... args) throws Exception {
        rabbitConnection.startConsuming();
    }
}

