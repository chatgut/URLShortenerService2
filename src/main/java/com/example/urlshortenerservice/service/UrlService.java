package com.example.urlshortenerservice.service;

import com.example.urlshortenerservice.model.Url;
import com.example.urlshortenerservice.model.UrlDto;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public interface UrlService {

    public Url generateSHortLink(UrlDto urlDto);
    public Url persistHortLInk(Url url);
    public Url getEncodedURL(String url);
    public void deleteShortLink(Url url);

}
