package info.donsun.cache.filecache.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "filecache")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileCacheConfig {

    @JacksonXmlProperty(isAttribute = true)
    private Long defaultExpireTime;

    @JacksonXmlProperty(isAttribute = true)
    private String defaultDir;

    private List<CacheConfig> caches;

    public Long getDefaultExpireTime() {
        return defaultExpireTime;
    }

    public void setDefaultExpireTime(Long defaultExpireTime) {
        this.defaultExpireTime = defaultExpireTime;
    }

    public String getDefaultDir() {
        return defaultDir;
    }

    public void setDefaultDir(String defaultDir) {
        this.defaultDir = defaultDir;
    }

    public List<CacheConfig> getCaches() {
        return caches;
    }

    public void setCaches(List<CacheConfig> caches) {
        this.caches = caches;
    }

}
