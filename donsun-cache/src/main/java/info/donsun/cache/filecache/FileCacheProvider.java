package info.donsun.cache.filecache;

import info.donsun.cache.CacheException;
import info.donsun.cache.CacheProvider;
import info.donsun.cache.filecache.config.CacheConfig;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * FileCache provider
 * 
 * @author Leo.du
 * @date 2013年9月14日
 */
public class FileCacheProvider implements CacheProvider {

    private static final Logger LOG = Logger.getLogger(FileCacheProvider.class);

    /**
     * Native cache manager
     */
    private Map<String, FileCache> cacheManager;

    private FileCacheManager manager;

    @Override
    public synchronized FileCache buildCache(String name) throws CacheException {
        FileCache fileCache = cacheManager.get(name);
        if (fileCache == null) {

            CacheConfig cache = manager.getCache(name);
            if (cache == null) {
                LOG.warn("Could not find configuration [" + name + "]; using defaults.");
                manager.addCache(name);
                cache = manager.getCache(name);
                LOG.debug("started FileCache region: " + name);
            }
            fileCache = new FileCache(cache);
            cacheManager.put(name, fileCache);
            return fileCache;

        }
        return fileCache;
    }

    @Override
    public synchronized void create(String configurationFileName) throws CacheException {
        if (manager != null) {
            LOG.warn("Attempt to restart an already started FileCacheProvide");
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

            manager = FileCacheManager.create(inputStream);
        } else {
            manager = FileCacheManager.create();
        }
        cacheManager = new Hashtable<String, FileCache>();
    }

    @Override
    public synchronized void destroy() throws CacheException {
        if (cacheManager != null) {
            cacheManager.clear();
        }

        if (manager != null) {
            manager.removeAll();
        }

    }

}
