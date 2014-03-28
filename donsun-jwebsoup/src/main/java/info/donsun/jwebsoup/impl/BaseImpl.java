/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package info.donsun.jwebsoup.impl;

import info.donsun.jwebsoup.Connection.Base;
import info.donsun.jwebsoup.Connection.Cookie;
import info.donsun.jwebsoup.util.Validate;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of {@link Base}.
 * 
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-12-30
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class BaseImpl<T extends Base> implements Base<T> {

    protected Map<String, String> headers;
    protected Map<String, Cookie> cookies;
    protected String charset;

    public BaseImpl() {
        headers = new LinkedHashMap<String, String>();
        cookies = new LinkedHashMap<String, Cookie>();
    }

    public String header(String name) {
        Validate.notNull(name, "Header name must not be null");
        return getHeaderCaseInsensitive(name);
    }

    public T header(String name, String value) {
        Validate.notEmpty(name, "Header name must not be empty");
        Validate.notNull(value, "Header value must not be null");
        removeHeader(name); // ensures we don't get an "accept-encoding" and a
                            // "Accept-Encoding"
        headers.put(name, value);
        return (T) this;
    }
    
    public T headers(Map<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                header(entry.getKey(), entry.getValue());
            }
        }
        return (T) this;
    }

    public boolean hasHeader(String name) {
        Validate.notEmpty(name, "Header name must not be empty");
        return getHeaderCaseInsensitive(name) != null;
    }

    public T removeHeader(String name) {
        Validate.notEmpty(name, "Header name must not be empty");
        Map.Entry<String, String> entry = scanHeaders(name); // remove is case
                                                             // insensitive too
        if (entry != null)
            headers.remove(entry.getKey()); // ensures correct case
        return (T) this;
    }

    public Map<String, String> headers() {
        return headers;
    }

    private String getHeaderCaseInsensitive(String name) {
        Validate.notNull(name, "Header name must not be null");
        // quick evals for common case of title case, lower case, then scan for
        // mixed
        String value = headers.get(name);
        if (value == null) {
            value = headers.get(name.toLowerCase());
        }
        if (value == null) {
            Map.Entry<String, String> entry = scanHeaders(name);
            if (entry != null) {
                value = entry.getValue();
            }
        }
        return value;
    }

    private Map.Entry<String, String> scanHeaders(String name) {
        String lc = name.toLowerCase();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey().toLowerCase().equals(lc)) {
                return entry;
            }
        }
        return null;
    }

    public Cookie cookie(String name) {
        Validate.notNull(name, "Cookie name must not be null");
        return cookies.get(name);
    }

    public T cookie(Cookie cookie) {
        Validate.notNull(cookie, "Cookie value must not be null");
        cookies.put(cookie.name(), cookie);
        return (T) this;
    }

    public T cookies(Collection<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie cookie : cookies) {
                cookie(cookie);
            }
        }
        return (T) this;
    }

    public T cookies(Map<String, String> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                cookie(CookieImpl.create(entry.getKey(), entry.getValue()));
            }
        }
        return (T) this;
    }

    public boolean hasCookie(String name) {
        Validate.notEmpty(name, "Cookie name must not be empty");
        return cookies.containsKey(name);
    }

    public T removeCookie(String name) {
        Validate.notEmpty(name, "Cookie name must not be empty");
        cookies.remove(name);
        return (T) this;
    }

    public Collection<Cookie> cookies() {
        return cookies.values();
    }

    public String charset() {
        return charset;
    }

    public T charset(String charset) {
        Validate.notEmpty(charset, "Charset must not be empty");
        this.charset = charset;
        return (T) this;
    }

}
