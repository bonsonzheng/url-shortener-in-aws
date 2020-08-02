package bonsonzheng.url.shortener.service;

import bonsonzheng.url.shortener.data.UrlMap;
import bonsonzheng.url.shortener.db.CounterDao;
import bonsonzheng.url.shortener.db.UrlMapDao;
import bonsonzheng.url.shortener.util.Base62Encoder;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class UrlShortenService {

    private final Logger logger = LoggerFactory.getLogger(UrlShortenService.class);

    @Autowired
    private CounterDao counterDao;
    @Autowired
    private UrlMapDao urlMapDao;

    @Autowired
    private Base62Encoder base62Encoder;

    public static final long COUNTER_CHUNK_SIZE = 1000000;

    private AtomicLong currentCounter = new AtomicLong(-1);

    private Long currentCounterCeiling;

    public UrlShortenService(CounterDao counterDao, UrlMapDao urlMapDao, Base62Encoder base62Encoder) {
        this.counterDao = counterDao;
        this.urlMapDao = urlMapDao;
        this.base62Encoder = base62Encoder;
    }

    public String shortenUrl(String longUrl) throws Exception {
        if (currentCounter.longValue() == -1 || currentCounter.longValue() >= currentCounterCeiling) {
            retrieveNextCounterRange();
        }

        String base62String = base62Encoder.base62(currentCounter.getAndIncrement());
        String shortenedUrl = urlMapDao.putItemIfNotExists(longUrl, base62String);

        return shortenedUrl;
    }

    public String getUrl(String shortUrl) {
        String retrievedItem = urlMapDao.retrieveItem(shortUrl);
        return retrievedItem != null ? new Gson().fromJson(retrievedItem, UrlMap.class).getLongUrl() : null;
    }

    private void retrieveNextCounterRange() throws Exception{
        currentCounterCeiling = counterDao.incrementAndGet(COUNTER_CHUNK_SIZE);
        currentCounter = new AtomicLong(currentCounterCeiling - COUNTER_CHUNK_SIZE);

        logger.info("Retrieved counter from database range : " + currentCounter + " - " + currentCounterCeiling);
    }

}
