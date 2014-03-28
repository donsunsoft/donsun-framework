package info.donsun.cache.ehcache;

import info.donsun.cache.Cache;
import info.donsun.cache.CacheException;

import java.util.List;

import net.sf.ehcache.Element;

/**
 * EhCache cache
 * 
 * @author Leo.du
 * @date 2013年9月14日
 */
public class EhCache implements Cache {

    /**
     * EhCache instance
     */
    private net.sf.ehcache.Cache cache;

    /**
     * wrap EhCache instance
     * 
     * @param cache EhCache instance.
     */
    public EhCache(net.sf.ehcache.Cache cache) {
        this.cache = cache;
    }

    @Override
    public List<?> keys() throws CacheException {
        return this.cache.getKeys();
    }

    @Override
    public Object get(Object key) throws CacheException {
        try {
            if (key == null) {
                return null;
            } else {
                Element element = cache.get(key);
                if (element != null) {
                    return element.getObjectValue();
                }
            }
            return null;
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void put(Object key, Object value) throws CacheException {
        try {
            Element element = new Element(key, value);
            cache.put(element);
        } catch (IllegalArgumentException e) {
            throw new CacheException(e);
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }

    }

    @Override
    public void remove(Object key) throws CacheException {
        try {
            cache.remove(key);
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void clear() throws CacheException {
        try {
            cache.removeAll();
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void destroy() throws CacheException {
        try {
            cache.getCacheManager().removeCache(cache.getName());
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }

}