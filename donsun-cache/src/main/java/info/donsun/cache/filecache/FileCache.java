/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package info.donsun.cache.filecache;

import info.donsun.cache.Cache;
import info.donsun.cache.CacheException;
import info.donsun.cache.filecache.config.CacheConfig;
import info.donsun.core.utils.Values;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-10-9
 * 
 */
public class FileCache implements Cache {

    protected Logger log = Logger.getLogger(FileCache.class.getName());

    private CacheConfig config;

    private static final Object LOCK = new Object();

    /**
     * @param cache
     */
    public FileCache(CacheConfig config) {
        this.config = config;
    }

    @Override
    public Object get(Object key) throws CacheException {
        synchronized (LOCK) {
            // 如果存在，返回缓存
            if (exists(key)) {
                // 遍历缓存文件
                for (String fileName : listFileNames()) {
                    if (isCacheFile(key, fileName)) {
                        try {
                            FileInputStream fileStream = new FileInputStream(FileUtils.getFile(config.getDir(), fileName));
                            ObjectInputStream in = new ObjectInputStream(fileStream);
                            boolean isExpire = false;
                            try {
                                CachedObject cachedObject = (CachedObject) in.readObject();
                                // 缓存已过期
                                if (cachedObject.getExpire() > 0 && cachedObject.getExpire() < System.currentTimeMillis()) {
                                    isExpire = true;
                                    return null;
                                }
                                return cachedObject.getData();
                            } finally {
                                in.close();
                                fileStream.close();
                                if (isExpire) {
                                    remove(key);
                                }
                            }
                        } catch (Exception e) {
                            throw new CacheException("Get cache file fail.", e);
                        }
                    }
                }
            }

            return null;
        }
    }

    @Override
    public void put(Object key, Object value) throws CacheException {
        synchronized (LOCK) {
            // 删除历史缓存
            remove(key);

            // 将id编码成标准格式
            String fileName = formatId(key);

            // 如果ttl<0，文件名后面不跟过期时间，永久保存

            // 创建目录
            File cacheDir = new File(config.getDir());
            if (!cacheDir.exists() || !cacheDir.isDirectory()) {
                try {
                    FileUtils.forceMkdir(cacheDir);
                } catch (IOException e) {
                    throw new CacheException(String.format("Create cacheDir %s fail.", cacheDir.getPath()));
                }
            }

            // 创建文件
            File file = FileUtils.getFile(config.getDir(), fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new CacheException("Create new file " + file.getName() + " fail.", e);
                }
            }

            try {
                // 序列化对象
                FileOutputStream fileStream = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileStream);

                try {
                    CachedObject cachedObject = new CachedObject();
                    if (config.getExpireTime() != null) {
                        cachedObject.setExpire(System.currentTimeMillis() + config.getExpireTime());
                    }
                    cachedObject.setKey(key);
                    cachedObject.setData(value);
                    out.writeObject(cachedObject);
                } finally {
                    out.close();
                    fileStream.close();

                }
            } catch (Exception e) {
                throw new CacheException("Save cache file fail.", e);
            }
        }
    }

    @Override
    public List<Object> keys() throws CacheException {
        synchronized (LOCK) {
            List<Object> keys = new ArrayList<Object>();
            for (String fileName : listFileNames()) {
                try {
                    File file = FileUtils.getFile(config.getDir(), fileName);

                    if (!file.exists()) {
                        continue;
                    }

                    FileInputStream fileStream = new FileInputStream(file);
                    ObjectInputStream in = new ObjectInputStream(fileStream);
                    boolean isExpire = false;
                    try {

                        CachedObject cachedObject = (CachedObject) in.readObject();
                        // 缓存已过期
                        if (cachedObject.getExpire() > 0 && cachedObject.getExpire() < System.currentTimeMillis()) {
                            isExpire = true;
                            continue;
                        }

                        keys.add(cachedObject.getKey());
                    } finally {
                        in.close();
                        fileStream.close();
                        if (isExpire) {
                            FileUtils.forceDelete(file);
                        }
                    }
                } catch (Exception e) {
                    throw new CacheException("Get cache file fail.", e);
                }

            }

            return keys;

        }
    }

    /**
     * 判断缓存文件是否存在
     * 
     * @throws Exception
     * 
     */
    public boolean exists(Object key) {
        synchronized (LOCK) {
            // 遍历缓存文件
            for (String fileName : listFileNames()) {
                if (isCacheFile(key, fileName)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 判断缓存文件是否属于指定key
     * 
     * @param key
     * @param fileName
     * @return
     */
    private boolean isCacheFile(Object key, String fileName) {
        return fileName.toLowerCase().equals(formatId(key));
    }

    /**
     * 获取格式化成MD5形式的key
     * 
     * @param key
     * @return
     */
    private String formatId(Object key) {
        return DigestUtils.md5Hex(Values.getString(key).toLowerCase());
    }

    private List<String> listFileNames() {
        String[] list = new File(config.getDir()).list();
        List<String> files = new ArrayList<String>();
        if (list != null && list.length > 0) {
            for (String file : list) {
                files.add(file);
            }
        }

        return files;
    }

    @Override
    public void remove(Object key) throws CacheException {
        synchronized (LOCK) {
            // 遍历缓存文件并删除
            for (String fileName : listFileNames()) {
                if (isCacheFile(key, fileName)) {
                    File file = FileUtils.getFile(config.getDir(), fileName);
                    try {
                        FileUtils.forceDelete(file);
                        break;
                    } catch (IOException e) {
                        throw new CacheException("Delete file " + config.getDir() + " fail.", e);
                    }
                }
            }
        }
    }

    @Override
    public void clear() throws CacheException {
        synchronized (LOCK) {
            try {
                FileUtils.cleanDirectory(new File(config.getDir()));
            } catch (IOException e) {
                throw new CacheException("Clean directory " + config.getDir() + " fail.", e);
            }
        }
    }

    @Override
    public void destroy() throws CacheException {
        synchronized (LOCK) {
            try {
                FileUtils.deleteDirectory(new File(config.getDir()));
            } catch (IOException e) {
                throw new CacheException("Delete directory " + config.getDir() + " fail.", e);
            }
        }
    }

}
