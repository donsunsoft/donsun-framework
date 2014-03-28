package info.donsun.cache.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CacheWaitDieTest extends CacheBaseTest {

    private static final String NAME = "shortLivedCache";

    @Test
    public void testSetThenToIdle() throws Exception {
        String key = "key1";
        String value = "test";
        super.getCache().set(NAME, key, value);

        // then get
        Object object = super.getCache().get(NAME, key);
        assertNotNull(object);
        assertEquals(value, String.valueOf(object));

        // sleep 2 second, in ehcache.xml this cache setting to idle 2 second
        Thread.sleep(2 * 1100);

        // then get again
        assertNull(super.getCache().get(NAME, key));
    }

    @Test
    public void testSetThenToDie() throws Exception {
        String key = "key2";
        String value = "test";
        super.getCache().set(NAME, key, value);

        // then get
        Object object = super.getCache().get(NAME, key);
        assertNotNull(object);
        assertEquals(value, String.valueOf(object));

        // after 4 second
        long start = System.currentTimeMillis();
        for (;;) {
            boolean isDie = null == super.getCache().get(NAME, key);
            if (isDie) {
                break;
            } else {
                //to nothing
            }
        }
        long end = System.currentTimeMillis();
        assertTrue(end - start - 4 * 1000 <= 5);

    }

}
