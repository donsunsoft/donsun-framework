package info.donsun.cache.ehcache;

import info.donsun.cache.CacheException;
import info.donsun.cache.CacheProvider;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;

/**
 * EhCache provider
 * 
 * @author Leo.du
 * @date 2013年9月14日
 */
public class EhCacheProvider implements CacheProvider {

    private static final Logger LOG = Logger.getLogger(EhCacheProvider.class);

    /**
     * EhCache manager
     */
    private CacheManager manager;

    /**
     * Native cache manager
     */
    private Map<String, EhCache> cacheManager;

    @Override
    public synchronized EhCache buildCache(String name) throws CacheException {
        EhCache ehcache = cacheManager.get(name);
        if (ehcache == null) {
            try {
                net.sf.ehcache.Cache cache = manager.getCache(name);
                if (cache == null) {
                    LOG.warn("Could not find configuration [" + name + "]; using defaults.");
                    manager.addCache(name);
                    cache = manager.getCache(name);
                    LOG.debug("started EHCache region: " + name);
                }
                
                ehcache = new EhCache(cache);
                cacheManager.put(name, ehcache);
                return ehcache;
            } catch (net.sf.ehcache.CacheException e) {
                throw new CacheException(e);
            }
        }
        return ehcache;
    }

    @Override
    public synchronized void start(String configurationFileName) throws CacheException {
        if (manager != null) {
            LOG.warn("Attempt to restart an already started EhCacheProvider. Use sessionFactory.close() "
                    + " between repeated calls to buildSessionFactory. Using previously created EhCacheProvider."
                    + " If this behaviour is required, consider using net.sf.ehcache.hibernate.SingletonEhCacheProvider.");
            return;
        }

        InputStream inputStream = null;

        if (configurationFileName != null) {

            // 尝试读取文件系统中的配置文件
            try {
                inputStream = new FileInputStream(configurationFileName);
            } catch (Exception e) {
            }

            if (inputStream == null) {
                // 尝试读取classpath中的配置文件
                try {
                    inputStream = this.getClass().getClassLoader().getResourceAsStream(configurationFileName);
                } catch (Exception e) {
                }
            }
        }

        if (inputStream != null) {
            manager = CacheManager.create(inputStream);
        } else {
            manager = CacheManager.create();
        }
        cacheManager = new Hashtable<String, EhCache>();
    }

    @Override
    public synchronized void stop() {
        if (manager != null) {
            manager.shutdown();
            manager = null;
        }
    }

}
