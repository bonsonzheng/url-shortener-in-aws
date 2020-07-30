package bonsonzheng.url.shortener.controller;


import bonsonzheng.url.shortener.data.UrlMap;
import bonsonzheng.url.shortener.data.UrlShortenReq;
import bonsonzheng.url.shortener.service.UrlShortenService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlShortenerController {

    @Autowired
    UrlShortenService urlShortenService;

    public UrlShortenerController(UrlShortenService urlShortenService) {
        this.urlShortenService = urlShortenService;
    }

    @CrossOrigin
    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getLongUrl(@PathVariable String shortUrl) {
        System.out.println("short url : " + shortUrl);
        String longUrl = urlShortenService.getUrl(shortUrl);
        System.out.println(shortUrl + " :::::: " + longUrl);
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(URI.create(longUrl)).build();
    }

    @PostMapping("/url-map")
    public ResponseEntity<String> shorten(@RequestBody UrlShortenReq urlShortenReq) throws Exception {
        String shortenUrl = urlShortenService.shortenUrl(urlShortenReq.getLongUrl());
        String bodyJson = new Gson().toJson(new UrlMap(urlShortenReq.getLongUrl(),shortenUrl));
        System.out.println(urlShortenReq.getLongUrl() + " ----> " + shortenUrl);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(bodyJson);
    }

}
