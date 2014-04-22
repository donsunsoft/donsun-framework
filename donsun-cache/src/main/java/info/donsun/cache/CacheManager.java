package info.donsun.cache;

import java.io.Serializable;
import java.util.List;

/**
 * Cache delegate for all kinds of cache
 * 
 * @author Leo.du
 * @date 2013年9月14日
 */
public interface CacheManager {

    /**
     * get cache object from cache name by cache key
     * 
     * @param name cache name
     * @param key cache key
     * @return the cached object or null
     */
    <T> T get(String name, Serializable key);

    /**
     * set cache value to the cache name with cache key
     * 
     * @param name cache name
     * @param key cache key
     * @param value the cached value
     */
    void set(String name, Serializable key, Object value);

    /**
     * remove cache key from cache name
     * 
     * @param name cache name
     * @param key cache key
     */
    void remove(String name, Serializable key);

    /**
     * list all keys in cache name
     * 
     * @param name cache name
     */
    List<?> keys(String name);

    /**
     * remove all caches.
     * 
     * @param name
     */
    void clear(String name);

    /**
     * delete the cache holder
     * 
     * @param name
     */
    void destroy(String name);

}
