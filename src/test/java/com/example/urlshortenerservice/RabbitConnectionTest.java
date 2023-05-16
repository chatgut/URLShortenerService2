package com.example.urlshortenerservice;

import static org.junit.jupiter.api.Assertions.*;

class RabbitConnectionTest {

    public static void main(String[] args) {


        RabbitConnection rabbitConnection = new RabbitConnection();

        rabbitConnection.publishMessage();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}