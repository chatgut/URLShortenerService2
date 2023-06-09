package com.example.urlshortenerservice.Controller;

import com.example.urlshortenerservice.entity.Url;
import com.example.urlshortenerservice.dto.UrlDto;
import com.example.urlshortenerservice.dto.UrlErrorReponseDto;
import com.example.urlshortenerservice.dto.UrlResponseDto;
import com.example.urlshortenerservice.url.UrlShort;
import com.example.urlshortenerservice.service.UrlService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;


@CrossOrigin
@RestController
public class UrlShorteningController {


    @Autowired
    private UrlService urlService;


    @PostMapping("/urlShortener")
    public UrlShort generateShortLink(@RequestBody UrlDto urlDto) {
        Url urlToRet = urlService.generateSHortLink(urlDto);

        UrlShort urlShort = new UrlShort();


        if (urlToRet != null) {
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
            urlResponseDto.setExpirationDate(urlToRet.getExpirationDate());
            urlResponseDto.setShortLink(urlToRet.getShortLink());

            urlShort.setShort_url(urlToRet.getShortLink());
            
            return urlShort;

        } else {
            UrlErrorReponseDto urlErrorReponseDto = new UrlErrorReponseDto();
            urlErrorReponseDto.setStatus("404");
            urlErrorReponseDto.setError("There was an error processing your request. Please try again!");
            return null;
        }
    }


    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(shortLink)) {
            UrlErrorReponseDto urlErrorReponseDto = new UrlErrorReponseDto();
            urlErrorReponseDto.setError("Url does not exist or it might have expired!");
            urlErrorReponseDto.setStatus("400");
            return new ResponseEntity<>(urlErrorReponseDto, HttpStatus.BAD_REQUEST);
        }

        Url urlToRet = urlService.getEncodedURL(shortLink);

        if (urlToRet.getExpirationDate().isBefore(LocalDateTime.now())) {
            urlService.deleteShortLink(urlToRet);
            UrlErrorReponseDto urlErrorReponseDto = new UrlErrorReponseDto();
            urlErrorReponseDto.setError("Url has expired. Please try generating a fresh one.");
            urlErrorReponseDto.setStatus("200");
            return new ResponseEntity<>(urlErrorReponseDto, HttpStatus.OK);
        }

        response.sendRedirect(urlToRet.getOriginalUrl());

        return ResponseEntity.ok().build();
    }
}



