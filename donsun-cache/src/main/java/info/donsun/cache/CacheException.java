package info.donsun.cache;

/**
 * Cache exception
 * 
 * @author Leo.du
 * @date 2013年9月14日
 */
public class CacheException extends RuntimeException {

    private static final long serialVersionUID = 3650210476869568321L;

    /**
     * Cache exception with message.
     * 
     * @param msg exception message
     */
    public CacheException(String msg) {
        super(msg);
    }

    /**
     *  Cache exception with message and exception.
     * 
     * @param msg exception message
     * @param exception exception object
     */
    public CacheException(String msg, Throwable exception) {
        super(msg, exception);
    }

    /**
     *  Cache exception with exception.
     * 
     * @param exception exception object
     */
    public CacheException(Throwable exception) {
        super(exception);
    }
    
}
