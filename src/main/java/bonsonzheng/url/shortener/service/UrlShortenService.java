package bonsonzheng.url.shortener.service;

import bonsonzheng.url.shortener.data.UrlMap;
import bonsonzheng.url.shortener.db.CounterDao;
import bonsonzheng.url.shortener.db.UrlMapDao;
import bonsonzheng.url.shortener.util.Base62Encoder;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class UrlShortenService {

    private static final long COUNTER_CHUNK_SIZE = 1000000;
    private AtomicLong currentCounter = new AtomicLong(-1);
    private CounterDao counterDao;
    private UrlMapDao urlMapDao;
    private Long currentCounterCeiling;
    private Base62Encoder base62Encoder;
    public UrlShortenService(CounterDao counterDao, UrlMapDao urlMapDao) {
        this.counterDao = counterDao;
        this.urlMapDao = urlMapDao;

        base62Encoder = new Base62Encoder();
        retrieveNextCounterRange();
    }

    public String shortenUrl(String longUrl) throws Exception {
        if(currentCounter.longValue() == -1 || currentCounter.longValue() >= currentCounterCeiling){
            retrieveNextCounterRange();
        }

        String base62String = base62Encoder.base62(currentCounter.getAndIncrement());
        String shortenedUrl = urlMapDao.putItemIfNotExists(longUrl, base62String);

        return shortenedUrl;
    }

    public String getUrl(String shortUrl){
        String retrievedItem = urlMapDao.retrieveItem(shortUrl);
        return retrievedItem != null ? new Gson().fromJson(retrievedItem, UrlMap.class).getLongUrl() : null;
    }

    private void retrieveNextCounterRange(){
        currentCounterCeiling = counterDao.incrementAndGet(COUNTER_CHUNK_SIZE);
        currentCounter = new AtomicLong(currentCounterCeiling - COUNTER_CHUNK_SIZE);
    }

}
