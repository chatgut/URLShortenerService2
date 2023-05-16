package com.example.urlshortenerservice;

import com.rabbitmq.client.*;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import org.json.JSONObject;

public class RabbitConnection {
    private Connection connection;
    private Channel channel;
    private String queueName;
    private String testMessage;

    public RabbitConnection() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(System.getenv().getOrDefault("ROCKET_RABBIT_HOST", "localhost"));
            factory.setPort(5672);

            connection = factory.newConnection();
            channel = connection.createChannel();
            queueName = "messages";

            channel.queueDeclare(queueName, false, false, false, null);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void startConsuming() {
        try {
            channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, StandardCharsets.UTF_8);


                    JSONObject jsonPayload = new JSONObject(message);


                    String extractedMessage = jsonPayload.getString("message");


                    testMessage = extractedMessage;

                    System.out.println("Received message: " + extractedMessage);


                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void publishMessage() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(System.getenv().getOrDefault("ROCKET_RABBIT_HOST", "localhost"));
            factory.setPort(5672);

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(queueName, false, false, false, null);

                JSONObject jsonAd = new JSONObject();
                String message = "hello";




                channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("Sent message: " + message);
            }
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}
