package bonsonzheng.url.shortener.controller;


import bonsonzheng.url.shortener.data.UrlMap;
import bonsonzheng.url.shortener.data.UrlShortenReq;
import bonsonzheng.url.shortener.service.UrlShortenService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    UrlShortenService urlShortenService;

    public UrlShortenerController(UrlShortenService urlShortenService) {
        this.urlShortenService = urlShortenService;
    }


    @CrossOrigin
    @GetMapping("/s/{shortUrl}")
    public ResponseEntity<String> getLongUrl(@PathVariable String shortUrl) {
        String longUrl = urlShortenService.getUrl(shortUrl);
        if(longUrl == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(URI.create(longUrl)).build();
    }

    @PostMapping(path = "/url-map", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> shorten(@RequestHeader HttpHeaders headers, @RequestBody UrlShortenReq urlShortenReq) throws Exception {
        String urlPrefix = getUrlPrefix(headers);
        String shortenUrl = urlPrefix + urlShortenService.shortenUrl(urlShortenReq.getLongUrl());
        String bodyJson = new Gson().toJson(new UrlMap(urlShortenReq.getLongUrl(), shortenUrl));
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(bodyJson);
    }

    private String getUrlPrefix(@RequestHeader HttpHeaders headers) {
        InetSocketAddress host = headers.getHost();
        return "http://" + host.getHostName() + ":" + host.getPort() + "/s/";
    }

}
