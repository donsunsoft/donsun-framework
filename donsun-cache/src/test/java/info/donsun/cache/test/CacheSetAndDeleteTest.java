package info.donsun.cache.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CacheSetAndDeleteTest extends CacheBaseTest {

    private static final String NAME = "simpleCache";

    @Test
    public void testSetThenDelete() throws Exception {
        String key = "key";
        String value = "test";
        super.getCache().set(NAME, key, value);

        // then get
        Object object = super.getCache().get(NAME, key);
        assertNotNull(object);
        assertEquals(value, String.valueOf(object));

        // then delete
        super.getCache().remove(NAME, key);

        // then get again
        assertNull(super.getCache().get(NAME, key));
    }

}
