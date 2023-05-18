package com.example.urlshortenerservice;

import com.example.urlshortenerservice.dto.UrlDto;
import com.example.urlshortenerservice.dto.UrlResponseDto;
import com.example.urlshortenerservice.entity.Url;
import com.example.urlshortenerservice.service.UrlService;
import com.google.common.hash.Hashing;
import com.rabbitmq.client.*;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;

public class RabbitConnection {
    private Connection connection;
    private Channel channel;
    private String queueName;
    private String testMessage;

    public String messageConvert;

    UrlResponseDto UrlResponseDto = new UrlResponseDto();
    UrlDto urlDto = new UrlDto();
    Url url = new Url();

    UrlService urlService = new UrlService() {
        @Override
        public Url generateSHortLink(UrlDto urlDto) {
            return null;
        }

        @Override
        public Url persistHortLInk(Url url) {
            return null;
        }

        @Override
        public Url getEncodedURL(String url) {
            return null;
        }

        @Override
        public void deleteShortLink(Url url) {

        }
    };


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

                    if (checkingForHttpAndHttps(extractedMessage)) {
                        System.out.println("Received message contains an HTTP URL from rabbitMQ: " + extractedMessage);
                        messageConvert = extractedMessage;

                        Url urlToRet = urlService.generateSHortLink(urlDto);


                        UrlResponseDto urlResponseDto = new UrlResponseDto();
                        urlResponseDto.setOriginalUrl(messageConvert);
                        urlResponseDto.setExpirationDate(LocalDateTime.now());
                        urlResponseDto.setShortLink(encodeUrl(messageConvert));


                        puttingInDatabase(urlResponseDto.getOriginalUrl(),urlResponseDto.getExpirationDate(),urlResponseDto.getShortLink());



//                        System.out.println("Convert Short Link: " + urlResponseDto.getShortLink());
//
//                        publishMessage(urlResponseDto.getShortLink());

                    } else {
                        System.out.println("Received message DOES not contain an http URl: " + extractedMessage);
                    }


                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void puttingInDatabase(String orginalUrl, LocalDateTime expiredTide, String shortLink){
        UrlResponseDto urlResponseDto = new UrlResponseDto();
        System.out.println(orginalUrl);
        System.out.println(expiredTide);
        System.out.println(shortLink);


    }

    private String encodeUrl(String url) {
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return encodedUrl;
    }


    private boolean checkingForHttpAndHttps(String extractedMessage) {
        String httpUrlPattern = "(?i)\\bhttps?://\\S+\\b";

        return extractedMessage.matches(httpUrlPattern);
    }

    public void publishMessage(String sendingMessage) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(System.getenv().getOrDefault("ROCKET_RABBIT_HOST", "localhost"));
            factory.setPort(5672);

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(queueName, false, false, false, null);

                JSONObject jsonAd = new JSONObject();
                jsonAd.put("message", sendingMessage);

                String message = jsonAd.toString();


                channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("Sent message to RabbatMQ: " + message);
            }
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}

