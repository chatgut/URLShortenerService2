package com.example.urlshortenerservice.reposiotry;

import com.example.urlshortenerservice.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url,Long> {

    public Url findByShortLink(String shortLink);

}
