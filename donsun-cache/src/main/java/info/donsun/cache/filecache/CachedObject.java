package info.donsun.cache.filecache;

import java.io.Serializable;

public class CachedObject implements Serializable {

    private static final long serialVersionUID = -7561131018970219281L;

    private long expire;

    private Object key;

    private Object data;

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
