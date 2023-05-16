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

                    // Parse the JSON payload
                    JSONObject jsonPayload = new JSONObject(message);

                    // Extract the "message" field
                    String extractedMessage = jsonPayload.getString("message");

                    System.out.println("Received message: " + extractedMessage);

                    // Process the received message here
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
