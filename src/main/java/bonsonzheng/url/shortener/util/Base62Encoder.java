package bonsonzheng.url.shortener.util;


public class Base62Encoder {

    static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    static final long MAX_SUPPORTED_NUMBER= 3521614606207L;

    public String base62(long value) {
        if(value < 0 || value > MAX_SUPPORTED_NUMBER){
            throw new IllegalArgumentException(value + " is out of supported range [0 - " + MAX_SUPPORTED_NUMBER + "]");
        }

        final StringBuilder sb = new StringBuilder(1);
        do {
            sb.insert(0, BASE62[(int) (value%62)]);
            value /= 62;
        } while (value > 0);

        return sb.toString();
    }

}
