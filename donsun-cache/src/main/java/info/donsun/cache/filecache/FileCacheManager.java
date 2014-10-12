/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package info.donsun.cache.filecache;

import info.donsun.cache.filecache.config.CacheConfig;
import info.donsun.cache.filecache.config.FileCacheConfig;
import info.donsun.core.utils.Collections3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * File cache manager.
 * 
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-10-9
 */
public class FileCacheManager {

    private static final Logger LOG = LoggerFactory.getLogger(FileCacheManager.class);

    /**
     * The Singleton Instance.
     */
    private static volatile FileCacheManager singleton;

    /**
     * Default cache file
     */
    public static final String DEFAULT_CONFIG_FILE = "filecache.xml";

    /**
     * Cache instance
     */
    private static Map<String, CacheConfig> caches = new Hashtable<String, CacheConfig>();

    private static FileCacheConfig fileCacheConfig;

    /**
     * Get cache config by cache name
     * 
     * @param name cache name
     * @return cache config
     */
    public CacheConfig getCache(String name) {
        return caches.get(name);
    }

    /**
     * Add new cache, if cache name exist, then ignore.
     * 
     * @param name cache name
     */
    public void addCache(String name) {
        if (null == getCache(name)) {
            CacheConfig tmpCacheConfig = new CacheConfig();
            tmpCacheConfig.setName(name);
            tmpCacheConfig.setDir(fileCacheConfig.getDefaultDir());
            tmpCacheConfig.setExpireTime(fileCacheConfig.getDefaultExpireTime());
            caches.put(name, tmpCacheConfig);
        }
    }

    /**
     * Create file cache manager with give config file.
     * 
     * @param resourceAsStream config file stream
     * @return cache manager
     */
    public static FileCacheManager create(InputStream resourceAsStream) {
        if (singleton != null) {
            LOG.debug("Attempting to create an existing singleton. Existing singleton returned.");
            return singleton;
        }

        synchronized (FileCacheManager.class) {
            if (singleton == null) {
                parseConfigFile(resourceAsStream);
                singleton = new FileCacheManager();
            } else {
                LOG.debug("Attempting to create an existing singleton. Existing singleton returned.");
            }
        }

        return singleton;
    }

    /**
     * Create default file cache manager.
     * 
     * @return cache manger instance
     */
    public static FileCacheManager create() {
        return create(FileCacheManager.class.getResourceAsStream(DEFAULT_CONFIG_FILE));
    }

    /**
     * Parse cache config file
     * 
     * @param resourceAsStream
     * @return
     */
    private static void parseConfigFile(InputStream resourceAsStream) {
        try {

            XmlMapper mapper = new XmlMapper();

            fileCacheConfig = mapper.reader(FileCacheConfig.class).readValue(resourceAsStream);
            String defaultDir = fileCacheConfig.getDefaultDir();
            Long defaultExpireTime = fileCacheConfig.getDefaultExpireTime();

            List<CacheConfig> cacheConfigs = fileCacheConfig.getCaches();
            if (cacheConfigs != null && cacheConfigs.size() > 0) {
                for (CacheConfig cache : cacheConfigs) {
                    Assert.notNull(cache.getName(), "Cache name must not be null.");

                    if (StringUtils.isBlank(cache.getDir())) {
                        cache.setDir(defaultDir);
                    }
                    if (cache.getExpireTime() == null) {
                        cache.setExpireTime(defaultExpireTime);
                    }
                    caches.put(cache.getName(), cache);
                }
            }
        } catch (JsonProcessingException e) {
            LOG.error("Parse cache config file exception.", e);
        } catch (IOException e) {
            LOG.error("Reade cache config file exception.", e);
        } catch (IllegalArgumentException e) {
            LOG.error("Illegal argument of cache name." + e.getMessage(), e);
        }
    }

    public void removeAll() {
        // delete default dir
        if (fileCacheConfig.getDefaultDir() != null) {
            deleteDir(fileCacheConfig.getDefaultDir());
        }

        List<CacheConfig> configs = fileCacheConfig.getCaches();
        if (Collections3.isNotEmpty(configs)) {
            for (CacheConfig config : configs) {
                if (config.getDir() != null) {
                    deleteDir(config.getDir());
                }
            }
        }
    }

    private void deleteDir(String dir) {
        File file = new File(dir);
        if (file.exists()) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                LOG.error(String.format("Delete %s fail", dir), e);
            }
        }
    }

}
