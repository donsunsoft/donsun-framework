package info.donsun.cache.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import info.donsun.cache.CacheManager;
import info.donsun.cache.DefaultCacheManager;
import info.donsun.cache.filecache.FileCacheProvider;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CacheFileTest {

    private static final String NAME = "fileCache";

    private CacheManager cache;

    @Before
    public void setUp() throws Exception {
        cache = new DefaultCacheManager(FileCacheProvider.class, "filecache.xml");
    }

    @Test
    public void testOnlyOneFile() throws Exception {
        String key = "key";
        String value = "test";
        cache.set(NAME, key, value);

        // then get
        Object object = cache.get(NAME, key);
        assertNotNull(object);
        assertEquals(value, String.valueOf(object));
    }

    @Test
    public void testManyToFile() throws Exception {
        String key = "key";
        String value = "test";
        for (int i = 1; i <= 10; i++) {
            cache.set(NAME, key + i, value + i);
        }

        // then get
        Object object = cache.get(NAME, key + 2);
        assertNotNull(object);
        assertEquals(value + 2, String.valueOf(object));
    }

    @Test
    public void testClear() {
        cache.clear(NAME);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testKeys() {

        cache.set(NAME, "aa", new String("你好"));

        cache.set(NAME, "bb", new String("hehe"));

        List<String> keys = (List<String>) cache.keys(NAME);

        System.out.println(keys);

        Object o = cache.get(NAME, "aa");
        System.out.println(o);
    }
}
