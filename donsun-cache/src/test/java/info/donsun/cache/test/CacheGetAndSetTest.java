package info.donsun.cache.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CacheGetAndSetTest extends CacheBaseTest {

    private static final String NAME = "simpleCache";

    @Test
    public void testSimpleString() throws Exception {
        String key = "key";
        String value = "test";
        super.getCache().set(NAME, key, value);

        // then get
        Object object = super.getCache().get(NAME, key);
        assertNotNull(object);
        assertEquals(value, String.valueOf(object));
    }
    
    @Test
    public void testSimpleObject() throws Exception {
        String key = "key";
        Simple sample = new Simple(1, "test");
        super.getCache().set(NAME, key, sample);
        
        // then get
        Object object = super.getCache().get(NAME, key);
        assertNotNull(object);
        assertEquals(sample.getId(), ((Simple)object).getId());
    }

    @Test
    public void testComplexObject() throws Exception {
        String key = "key";
        Simple simple = new Simple(1, "test");
        Complex complex = new Complex(1, simple);
        super.getCache().set(NAME, key, complex);

        // then get
        Object object = super.getCache().get(NAME, key);
        assertNotNull(object);
        
        Complex complexCache = (Complex)object;
        assertEquals(complex.getId(), complexCache.getId());
        
        Simple simpleCache = complexCache.getSimple();
        assertNotNull(simpleCache);
        assertEquals(simple.getId(), simpleCache.getId());
    }

}
