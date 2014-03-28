package info.donsun.cache;

/**
 * Cache provider interface
 * 
 * @author Leo.du
 * @date 2013年9月14日
 */
public interface CacheProvider {

    /**
     * configure the cache
     * 
     * @param regionName the name of the cache region
     * @throws CacheException cache exception
     * 
     * @return one cache
     */
    Cache buildCache(String regionName) throws CacheException;

    /**
     * start cache manager
     * 
     * @param configurationFileName configuration file name
     * @throws CacheException cache exception
     */
    void start(String configurationFileName) throws CacheException;

    /**
     * stop cache manager
     */
    void stop();

}
