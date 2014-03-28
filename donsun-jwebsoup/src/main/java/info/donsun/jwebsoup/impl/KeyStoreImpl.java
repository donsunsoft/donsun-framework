/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package info.donsun.jwebsoup.impl;

import info.donsun.jwebsoup.Connection.KeyStore;
import info.donsun.jwebsoup.util.Validate;

/**
 * Implementation of {@link KeyStore}.
 * 
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-12-30
 * 
 */
public final class KeyStoreImpl implements KeyStore {
    private String path;
    private String password;

    public static KeyStore create(String path, String password) {
        Validate.notEmpty(path, "KeyStore path must not be empty");
        Validate.notNull(password, "KeyStore password must not be null");
        return new KeyStoreImpl(path, password);
    }

    private KeyStoreImpl(String path, String password) {
        this.path = path;
        this.password = password;
    }

    public String path() {
        return path;
    }

    public KeyStore path(String path) {
        Validate.notEmpty(path, "KeyStore path must not be empty");
        this.path = path;
        return this;
    }

    public String password() {
        return password;
    }

    public KeyStore password(String password) {
        Validate.notNull(password, "KeyStore password must not be null");
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return path + "=" + password;
    }
}