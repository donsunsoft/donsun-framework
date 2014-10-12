package info.donsun.cache;

import java.util.List;

/**
 * Cache interface
 * 
 * @author Leo.du
 * @date 2013年9月14日
 */
public interface Cache {

    /**
     * get a object from the cache
     * 
     * @param key cache key
     * @return the cached object or <tt>null</tt>
     * @throws CacheException cache exception
     */
    Object get(Object key) throws CacheException;

    /**
     * put a object to the cache
     * 
     * @param key cache key
     * @param value cache value
     * @throws CacheException cache exception
     */
    void put(Object key, Object value) throws CacheException;

    /**
     * list all cache key
     * 
     * @return all cached key
     * @throws CacheException cache exception
     */
    List<?> keys() throws CacheException;

    /**
     * remove the cache by key
     * 
     * @param key cache key
     * @throws CacheException cache exception
     */
    void remove(Object key) throws CacheException;

    /**
     * clear this cache
     * 
     * @throws CacheException cache exception
     */
    void clear() throws CacheException;

}
