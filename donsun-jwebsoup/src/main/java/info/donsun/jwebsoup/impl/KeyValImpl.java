/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package info.donsun.jwebsoup.impl;

import info.donsun.jwebsoup.Connection.KeyVal;
import info.donsun.jwebsoup.util.Validate;

/**
 * 
 * Implementation of {@link KeyVal}.
 * 
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-12-30
 * 
 */
public final class KeyValImpl implements KeyVal {
    private String key;
    private String value;

    public static KeyVal create(String key, String value) {
        Validate.notEmpty(key, "Data key must not be empty");
        if (value == null) {
            value = "";
        }
        return new KeyValImpl(key, value);
    }

    private KeyValImpl(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyVal key(String key) {
        Validate.notEmpty(key, "Data key must not be empty");
        this.key = key;
        return this;
    }

    public String key() {
        return key;
    }

    public KeyVal value(String value) {
        Validate.notNull(value, "Data value must not be null");
        this.value = value;
        return this;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
