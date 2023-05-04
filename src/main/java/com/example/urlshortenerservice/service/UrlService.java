package com.example.urlshortenerservice.service;

import com.example.urlshortenerservice.entity.Url;
import com.example.urlshortenerservice.dto.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {

    public Url generateSHortLink(UrlDto urlDto);
    public Url persistHortLInk(Url url);
    public Url getEncodedURL(String url);
    public void deleteShortLink(Url url);

}
