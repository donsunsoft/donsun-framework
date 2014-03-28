/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package info.donsun.cache.filecache.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-10-9
 */
@JacksonXmlRootElement(localName = "cache")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CacheConfig {

    /**
     * Cache name.
     */
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    /**
     * The time cache to live
     */
    private Long expireTime;

    /**
     * Cache save directory, default java.io.tmpdir.
     */
    private String dir;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "CacheConfig [name=" + name + ", dir=" + dir + ", expireTime=" + expireTime + "]";
    }

}
