package info.donsun.cache.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CacheSetAndUpdateTest extends CacheBaseTest {

    private static final String NAME = "simpleCache";

    @Test
    public void testSetThenUpdate() throws Exception {
        String key = "key";
        String value = "test";
        super.getCache().set(NAME, key, value);

        // then get
        Object object = super.getCache().get(NAME, key);
        assertNotNull(object);
        assertEquals(value, String.valueOf(object));

        // and set again
        String value2 = "test2";
        super.getCache().set(NAME, key, value2);

        // then get again
        Object object2 = super.getCache().get(NAME, key);
        assertNotNull(object2);
        assertEquals(value2, String.valueOf(object2));
    }

}
