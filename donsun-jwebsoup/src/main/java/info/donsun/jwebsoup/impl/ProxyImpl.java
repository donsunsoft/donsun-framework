/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package info.donsun.jwebsoup.impl;

import info.donsun.jwebsoup.Connection.Proxy;
import info.donsun.jwebsoup.util.Validate;

/**
 * Implementation of {@link Proxy}.
 * 
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-12-30
 * 
 */
public final class ProxyImpl implements Proxy {
    private String host;
    private int port;
    private String username;
    private String password;

    public static Proxy create(String host, int port) {
        Validate.notEmpty(host, "Proxy host must not be empty");
        return new ProxyImpl(host, port, null, null);
    }

    public static Proxy create(String host, int port, String username, String password) {
        Validate.notEmpty(host, "Proxy host must not be empty");
        Validate.notEmpty(username, "Proxy username must not be empty");
        return new ProxyImpl(host, port, username, password);
    }
    
    private ProxyImpl(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;

    }

    public String host() {
        return host;
    }

    public Proxy host(String host) {
        Validate.notEmpty(host, "Proxy host must not be empty");
        this.host = host;
        return this;
    }

    public int port() {
        return port;
    }

    public Proxy port(int port) {
        this.port = port;
        return this;
    }

    public String username() {
        return username;
    }

    public Proxy username(String username) {
        Validate.notNull(username, "Proxy username must not be null");
        this.username = username;
        return this;
    }

    public String password() {
        return password;
    }

    public Proxy password(String password) {
        Validate.notNull(username, "Proxy password must not be null");
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return host + ":" + port + "[" + username + "-" + password + "]";
    }
}
