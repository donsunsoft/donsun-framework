package info.donsun.cache.manager;

import info.donsun.cache.Cache;
import info.donsun.cache.CacheManager;
import info.donsun.cache.CacheProvider;
import info.donsun.cache.ehcache.EhCacheProvider;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Cache manager implement
 * 
 * @author Leo.du
 * @date 2013年9月14日
 */
public class DefaultCacheManager implements CacheManager {

    private static final Logger LOG = Logger.getLogger(DefaultCacheManager.class);

    /**
     * Cache provider
     */
    private static CacheProvider provider;

    /**
     * Default cache provider
     */
    private static final CacheProvider DEFAULT_CACHE_PROVIDER = new EhCacheProvider();

    /**
     * Default construct
     */
    public DefaultCacheManager() {
        this(DEFAULT_CACHE_PROVIDER, null);
    }

    /**
     * Default construct
     * 
     * @param configurationFileName configuration file name
     */
    public DefaultCacheManager(String configurationFileName) {
        this(DEFAULT_CACHE_PROVIDER, configurationFileName);
    }

    /**
     * Construct with cache provider
     * 
     * @param cacheProvider cache provider
     * @param configurationFileName configuration file name
     */
    public DefaultCacheManager(CacheProvider cacheProvider, String configurationFileName) {
        initCacheProvider(cacheProvider, configurationFileName);
    }

    /**
     * init cache with give provider
     * 
     * @param cacheProvider provider
     */
    private void initCacheProvider(final CacheProvider cacheProvider, final String configurationFileName) {
        try {
            DefaultCacheManager.provider = cacheProvider;
            DefaultCacheManager.provider.start(configurationFileName);

            LOG.info("Using CacheProvider : " + cacheProvider.getClass().getName());
        } catch (Exception e) {
            LOG.fatal("Unabled to initialize cache provider:" + cacheProvider + ", using ehcache default.", e);
            DefaultCacheManager.provider = new EhCacheProvider();
        }
    }

    /**
     * get cache by name, if cache not exist and autoCreate is true,<br>
     * then auto create one cache and return
     * 
     * @param cacheName cache name
     * @return one cache
     */
    private Cache getCache(String cacheName) {
        return provider.buildCache(cacheName);
    }

    @Override
    public final Object get(String name, Serializable key) {
        if (name != null && key != null) {
            return getCache(name).get(key);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T> T get(Class<T> clazz, String name, Serializable key) {
        if (name != null && key != null) {
            return (T) getCache(name).get(key);
        }
        return null;
    }

    @Override
    public final void set(String name, Serializable key, Object value) {
        if (name != null && key != null && value != null) {
            getCache(name).put(key, value);
        }
    }

    @Override
    public final void remove(String name, Serializable key) {
        if (name != null && key != null) {
            getCache(name).remove(key);
        }
    }

    @Override
    public final List<?> keys(String name) {
        if (name != null) {
            return getCache(name).keys();
        }
        return null;
    }

    @Override
    public final void clear(String name) {
        if (name != null) {
            getCache(name).clear();
        }

    }

}
