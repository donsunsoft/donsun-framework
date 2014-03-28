/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package info.donsun.jwebsoup.impl;

import info.donsun.jwebsoup.Connection.Cookie;
import info.donsun.jwebsoup.util.Validate;

import java.io.Serializable;
import java.util.Date;

/**
 * Implementation of {@link Cookie}.
 * 
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-12-30
 * 
 */
public final class CookieImpl implements Cookie, Serializable {
    private static final long serialVersionUID = -7285152091825479920L;

    private String name;
    private String value;
    private Date expires;
    private String domain;
    private String path;
    private boolean secure;

    public static Cookie create(String name, String value) {
        return create(name, value, null, null, null);
    }

    public static Cookie create(String name, String value, String domain, String path, Date expires) {
        Validate.notEmpty(name, "Cooke name must not be empty");
        Validate.notNull(value, "Cookie value must not be null");
        return new CookieImpl(name, value, domain, path, expires);
    }

    private CookieImpl(String name, String value, String domain, String path, Date expires) {
        this.name = name;
        this.value = value;
        this.domain = domain;
        this.path = path;
        this.expires = expires;
    }

    public String name() {
        return name;
    }

    public Cookie name(String name) {
        Validate.notEmpty(name, "Cooke name must not be empty");
        this.name = name;
        return this;
    }

    public String value() {
        return value;
    }

    public Cookie value(String value) {
        Validate.notNull(value, "Cookie value must not be null");
        this.value = value;
        return this;
    }

    public Date expires() {
        return expires;
    }

    public Cookie expires(Date expires) {
        Validate.notNull(value, "Cookie expires must not be null");
        this.expires = expires;
        return this;
    }

    public String domain() {
        return domain;
    }

    public Cookie domain(String domain) {
        Validate.notNull(value, "Cookie domain must not be null");
        this.domain = domain;
        return this;
    }

    public String path() {
        return path;
    }

    public Cookie path(String path) {
        Validate.notNull(value, "Cookie path must not be null");
        this.path = path;
        return this;
    }

    public boolean secure() {
        return secure;
    }

    public Cookie secure(boolean secure) {
        this.secure = secure;
        return this;
    }

    public boolean persistent() {
        return (null != expires);
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
