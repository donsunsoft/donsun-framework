/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package info.donsun.jwebsoup.impl;

import info.donsun.jwebsoup.Connection.KeyStore;
import info.donsun.jwebsoup.Connection.KeyVal;
import info.donsun.jwebsoup.Connection.HttpMethod;
import info.donsun.jwebsoup.Connection.Proxy;
import info.donsun.jwebsoup.Connection.Request;
import info.donsun.jwebsoup.util.Validate;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementation of {@link Request}.
 * 
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-12-30
 * 
 */
public class RequestImpl extends BaseImpl<Request> implements Request {

    private URL url;
    private HttpMethod method;
    private int timeoutMilliseconds;
    private int maxBodySizeBytes;
    private boolean followRedirects;
    private int maxRedirects;
    private boolean allowCircularRedirects;
    private Collection<KeyVal> data;
    private Object entity;
    private Proxy proxy;
    private KeyStore keyStore;

    public static RequestImpl create() {
        return new RequestImpl();
    }

    public RequestImpl() {
        timeoutMilliseconds = 10000;
        maxBodySizeBytes = 1024 * 1024; // 1MB
        followRedirects = true;
        maxRedirects = 8;
        allowCircularRedirects = true;
        data = new ArrayList<KeyVal>();
        method = HttpMethod.GET;
    }

    public URL url() {
        return url;
    }

    public Request url(URL url) {
        Validate.notNull(url, "URL must not be null");
        this.url = url;
        return this;
    }

    public HttpMethod method() {
        return method;
    }

    public Request method(HttpMethod method) {
        Validate.notNull(method, "Method must not be null");
        this.method = method;
        return this;
    }

    public Proxy proxy() {
        return proxy;
    }

    public Request proxy(Proxy proxy) {
        Validate.notNull(proxy, "Proy must not be null");
        this.proxy = proxy;
        return this;
    }

    public KeyStore keyStore() {
        return keyStore;
    }

    public Request keyStore(KeyStore keyStore) {
        Validate.notNull(keyStore, "KeyStore must not be null");
        this.keyStore = keyStore;
        return this;
    }

    public int timeout() {
        return timeoutMilliseconds;
    }

    public Request timeout(int millis) {
        Validate.isTrue(millis >= 0, "Timeout milliseconds must be 0 (infinite) or greater");
        timeoutMilliseconds = millis;
        return this;
    }

    public int maxBodySize() {
        return maxBodySizeBytes;
    }

    public Request maxBodySize(int bytes) {
        Validate.isTrue(bytes >= 0, "maxSize must be 0 (unlimited) or larger");
        maxBodySizeBytes = bytes;
        return this;
    }

    public boolean followRedirects() {
        return followRedirects;
    }

    public Request followRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    public boolean allowCircularRedirects() {
        return allowCircularRedirects;
    }

    public Request allowCircularRedirects(boolean allowCircularRedirects) {
        this.allowCircularRedirects = allowCircularRedirects;
        return this;
    }

    public int maxRedirects() {
        return maxRedirects;
    }

    public Request maxRedirects(int maxRedirects) {
        Validate.isTrue(maxRedirects >= 0, "maxRedirects must be 0 (unlimited) or larger");
        this.maxRedirects = maxRedirects;
        return this;
    }

    public Request data(KeyVal keyval) {
        Validate.notNull(keyval, "Key val must not be null");
        data.add(keyval);
        return this;
    }

    public Collection<KeyVal> data() {
        return data;
    }

    public Request entity(Object entity) {
        Validate.notNull(entity, "Entity must not be null");
        this.entity = entity;
        return this;
    }

    public Object entity() {
        return entity;
    }
}
