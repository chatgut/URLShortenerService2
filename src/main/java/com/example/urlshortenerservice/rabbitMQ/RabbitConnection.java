//package com.example.urlshortenerservice.rabbitMQ;
//
//import com.example.urlshortenerservice.dto.UrlDto;
//import com.example.urlshortenerservice.dto.UrlResponseDto;
//import com.example.urlshortenerservice.entity.Url;
//
//import com.example.urlshortenerservice.service.UrlService;
//import com.rabbitmq.client.*;
//
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.concurrent.TimeoutException;
//
//
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//
//
//
//@Configuration
//public class RabbitConnection {
//    private Connection connection;
//    private Channel channel;
//    private static final String QUEUE_NAME = "messages";
//    private static final String QUEUE_NAME_SENDING_MESSAGE = "shortUrl";
//
//    private String messageConvert;
//
//    UrlDto urlDto = new UrlDto();
//
//    @Autowired
//    public UrlService urlService;
//
//
//    public RabbitConnection() {
//        try {
//
//            ConnectionFactory factory = new ConnectionFactory();
//            factory.setHost(System.getenv().getOrDefault("ROCKET_RABBIT_HOST", "localhost"));
//            factory.setPort(5672);
//
//            connection = factory.newConnection();
//            channel = connection.createChannel();
//
//
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        } catch (IOException | TimeoutException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void startConsuming() {
//        try {
//            channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
//                @Override
//                public void handleDelivery(String consumerTag, Envelope envelope,
//                                           AMQP.BasicProperties properties, byte[] body) {
//                    String message = new String(body, StandardCharsets.UTF_8);
//
//
//                    JSONObject jsonPayload = new JSONObject(message);
//
//                    String extractedMessage = jsonPayload.getString("message");
//
//
//                    if (checkingForHttpAndHttps(extractedMessage)) {
//
//                        messageConvert = extractedMessage;
//                        UrlResponseDto urlResponseDto = addingInDatabaseMessage();
//
//                        publishMessage(urlResponseDto.getShortLink());
//                    }
//
//                }
//
//                private UrlResponseDto addingInDatabaseMessage() {
//                    urlDto.setUrl(messageConvert);
//
//                    Url urlToRet = urlService.generateSHortLink(urlDto);
//
//
//                    UrlResponseDto urlResponseDto = new UrlResponseDto();
//                    urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
//                    urlResponseDto.setExpirationDate(LocalDateTime.now());
//                    urlResponseDto.setShortLink(urlToRet.getShortLink());
//                    return urlResponseDto;
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean checkingForHttpAndHttps(String extractedMessage) {
//        String httpUrlPattern = "(?i)\\bhttps?://\\S+\\b";
//
//        return extractedMessage.matches(httpUrlPattern);
//    }
//
//    public void publishMessage(String sendingMessage) {
//        try {
//            ConnectionFactory factory = new ConnectionFactory();
//            factory.setHost(System.getenv().getOrDefault("ROCKET_RABBIT_HOST", "localhost"));
//            factory.setPort(5672);
//
//            try (Connection connection = factory.newConnection();
//                 Channel channel = connection.createChannel()) {
//                channel.queueDeclare("shortURL", false, false, false, null);
//
//                JSONObject jsonAd = new JSONObject();
//                jsonAd.put("message", sendingMessage);
//
//                String message = jsonAd.toString();
//
//
//                channel.basicPublish("", QUEUE_NAME_SENDING_MESSAGE, null, message.getBytes(StandardCharsets.UTF_8));
//                System.out.println("Sent message to RabbitMQ: " + message);
//            }
//        } catch (TimeoutException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
