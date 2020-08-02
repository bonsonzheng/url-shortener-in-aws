package bonsonzheng.url.shortener.service;

import bonsonzheng.url.shortener.db.CounterDao;
import bonsonzheng.url.shortener.db.UrlMapDao;
import bonsonzheng.url.shortener.util.Base62Encoder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static bonsonzheng.url.shortener.service.UrlShortenService.COUNTER_CHUNK_SIZE;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UrlShortenServiceTest {

    UrlShortenService urlShortenService;

    @Mock
    CounterDao counterDao;

    @Mock
    UrlMapDao urlMapDao;

    @Mock
    Base62Encoder base62Encoder;

    @Before
    public void setup() {
        initMocks(this);
        urlShortenService = new UrlShortenService(counterDao, urlMapDao, base62Encoder);
    }

    @Test
    public void testShortenUrl() throws Exception {
        String LONG_URL = "LONG_URL";
        String SHORT_URL = "SHORT_URL";

        when(base62Encoder.base62(anyLong())).thenReturn(SHORT_URL);
        when(urlMapDao.putItemIfNotExists(LONG_URL, SHORT_URL)).thenReturn(SHORT_URL);

        String newLongUrlMappingResult = urlShortenService.shortenUrl(LONG_URL);
        verify(counterDao, times(1)).incrementAndGet(COUNTER_CHUNK_SIZE);
        assertEquals(SHORT_URL, newLongUrlMappingResult);
    }

    @Test
    public void testReturningExistingValueIfMappingExists() throws Exception {
        String LONG_URL = "LONG_URL";
        String SHORT_URL = "SHORT_URL";
        String EXISTING_SHORT_URL_IN_DB = "EXISTING_SHORT_URL_IN_DB";

        when(base62Encoder.base62(anyLong())).thenReturn(SHORT_URL);
        when(urlMapDao.putItemIfNotExists(LONG_URL, SHORT_URL)).thenReturn(EXISTING_SHORT_URL_IN_DB);

        String newLongUrlMappingResult = urlShortenService.shortenUrl(LONG_URL);
        assertEquals(EXISTING_SHORT_URL_IN_DB, newLongUrlMappingResult);
    }

    @Test
    public void testGetExistedUrlMap() throws Exception {
        when(urlMapDao.retrieveItem("SHORT_URL")).thenReturn("{longUrl:LONG_URL, shortUrl:SHORT_URL}");
        String retrievedLongUrl = urlShortenService.getUrl("SHORT_URL");
        assertEquals("LONG_URL", retrievedLongUrl);
    }

    @Test
    public void testReturningNullIfUrlMapNotExists() throws Exception {
        when(urlMapDao.retrieveItem("SHORT_URL")).thenReturn(null);
        String retrievedLongUrl = urlShortenService.getUrl("SHORT_URL");
        assertNull(retrievedLongUrl);
    }
}