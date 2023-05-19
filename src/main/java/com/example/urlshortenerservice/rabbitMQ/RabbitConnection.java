package com.example.urlshortenerservice.rabbitMQ;

import com.example.urlshortenerservice.dto.UrlDto;
import com.example.urlshortenerservice.dto.UrlResponseDto;
import com.example.urlshortenerservice.entity.Url;
import com.example.urlshortenerservice.service.UrlService;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitConnection {
    private final ConnectionFactory connectionFactory;
    private final UrlService urlService;

    private static final String QUEUE_NAME = "messages";
    private static final String QUEUE_NAME_TO_PUBLIC = "shortUrl";


    @Autowired
    public RabbitConnection(@Value("${rocket.rabbit.host:localhost}") String rabbitHost,
                            @Value("${rocket.rabbit.port:5672}") int rabbitPort,
                            UrlService urlService) {
        this.urlService = urlService;

        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitHost);
        connectionFactory.setPort(rabbitPort);
    }

    public void startConsuming() {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, StandardCharsets.UTF_8);

                    JSONObject jsonPayload = new JSONObject(message);
                    String extractedMessage = jsonPayload.getString("message");

                    if (checkingForHttpAndHttps(extractedMessage)) {
                        String shortLink = generateShortLink(extractedMessage);
                        publishMessage(shortLink);
                    }
                }
            });
        } catch (TimeoutException | IOException e) {
            throw new RuntimeException("Error starting consumer", e);
        }
    }

    private boolean checkingForHttpAndHttps(String extractedMessage) {
        String httpUrlPattern = "(?i)\\bhttps?://\\S+\\b";
        return extractedMessage.matches(httpUrlPattern);
    }

    private String generateShortLink(String url) {
        UrlDto urlDto = new UrlDto();
        urlDto.setUrl(url);

        Url generatedUrl = urlService.generateSHortLink(urlDto);

        UrlResponseDto urlResponseDto = new UrlResponseDto();
        urlResponseDto.setOriginalUrl(generatedUrl.getOriginalUrl());
        urlResponseDto.setExpirationDate(LocalDateTime.now());
        urlResponseDto.setShortLink(generatedUrl.getShortLink());

        return urlResponseDto.getShortLink();
    }

    private void publishMessage(String shortLink) {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare("shortURL", false, false, false, null);

            JSONObject jsonAd = new JSONObject();
            jsonAd.put("message", shortLink);

            String message = jsonAd.toString();

            channel.basicPublish("", QUEUE_NAME_TO_PUBLIC, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent message to RabbitMQ: " + message);
        } catch (TimeoutException | IOException e) {
            throw new RuntimeException("Error publishing message", e);
        }
    }
}
