package bonsonzheng.url.shortener.util;

/**
 * Created by zhengbangsheng on 2020/7/26.
 */
public class Base62Encoder {

    static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();


    public static String base62(long value) {
        final StringBuilder sb = new StringBuilder(1);
        do {
            sb.insert(0, BASE62[(int) (value%62)]);
            value /= 62;
        } while (value > 0);
        return sb.toString();
    }

}
