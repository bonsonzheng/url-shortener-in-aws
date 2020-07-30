package bonsonzheng.url.shortener.data;

import java.net.URL;

/**
 * Created by zhengbangsheng on 2020/7/27.
 */
public class UrlMap {

    private String longUrl;

    private String shortUrl;

    public UrlMap(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
