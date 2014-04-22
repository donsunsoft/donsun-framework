package info.donsun.cache.test;

import info.donsun.cache.CacheManager;
import info.donsun.cache.DefaultCacheManager;

import org.junit.Before;

public class CacheBaseTest {

    private CacheManager cache;

    @Before
    public void setUp() throws Exception {
//        cache = new DefaultCacheManager();
        cache = new DefaultCacheManager("src/test/resources/ehcache/ehcache2.xml");
    }

    public CacheManager getCache() {
        return cache;
    }

}
