package bonsonzheng.url.shortener.util;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Base62EncoderTest {

    Base62Encoder base62Encoder;

    @Before
    public void setup(){
        base62Encoder = new Base62Encoder();
    }

    @Test
    public void testEncodingSuportedNumber(){
        String endcoded100 = base62Encoder.base62(100L);
        String endcoded99999999999 = base62Encoder.base62(99999999999L);
        String endcodedMaxNumber = base62Encoder.base62(3521614606207L);

        assertEquals("1c",endcoded100);
        assertEquals("1l9Zo9n",endcoded99999999999);
        assertEquals("zzzzzzz",endcodedMaxNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooLargeNumberShouldThrowException(){
        base62Encoder.base62(35216146062087L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberShouldThrowException(){
        base62Encoder.base62(-1);
    }

}