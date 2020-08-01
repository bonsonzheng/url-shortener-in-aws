package bonsonzheng.url.shortener.controller;


import bonsonzheng.url.shortener.data.UrlMap;
import bonsonzheng.url.shortener.data.UrlShortenReq;
import bonsonzheng.url.shortener.service.UrlShortenService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetSocketAddress;
import java.net.URI;

@RestController
public class UrlShortenerController {

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);

    @Autowired
    UrlShortenService urlShortenService;

    @CrossOrigin
    @GetMapping("/s/{shortUrl}")
    public ResponseEntity<String> getLongUrl(@PathVariable String shortUrl) {
        logger.info("Receive get request with url: " + shortUrl);
        String longUrl = urlShortenService.getUrl(shortUrl);
        if(longUrl == null){
            logger.info("No mapping for url: " + shortUrl);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        logger.info("returning long url : " + longUrl + " for " + shortUrl);
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(URI.create(longUrl)).build();
    }

    @PostMapping(path = "/url-map", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> shorten(@RequestHeader HttpHeaders headers, @RequestBody UrlShortenReq urlShortenReq) throws Exception {
        logger.info("Receive post request with url: " + urlShortenReq);
        String urlPrefix = getUrlPrefix(headers);
        String shortenUrl = urlPrefix + urlShortenService.shortenUrl(urlShortenReq.getLongUrl());
        String bodyJson = new Gson().toJson(new UrlMap(urlShortenReq.getLongUrl(), shortenUrl));
        logger.info("Returning url mapping " + bodyJson);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(bodyJson);
    }

    private String getUrlPrefix(@RequestHeader HttpHeaders headers) {
        InetSocketAddress host = headers.getHost();
        return "http://" + host.getHostName() + ":" + host.getPort() + "/s/";
    }

}
