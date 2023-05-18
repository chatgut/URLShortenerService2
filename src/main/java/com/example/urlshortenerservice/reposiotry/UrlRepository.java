package com.example.urlshortenerservice.reposiotry;

import com.example.urlshortenerservice.entity.Url;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url,Long> {

    public Url findByShortLink(String shortLink);




}
