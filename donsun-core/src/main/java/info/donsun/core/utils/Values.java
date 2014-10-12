/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package info.donsun.core.utils;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 转换对象成需要的值
 * 
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-7-13
 */
public final class Values {

    /**
     * 构造函数
     */
    private Values() {

    }

    /**
     * 将对象转换成字符串
     * 
     * @param obj 对象
     * @param defaultValue 默认值，当字符串为空时返回默认值
     * @return 字符串
     */
    public static String getString(Object obj, String defaultValue) {
        return ObjectUtils.toString(obj, defaultValue);
    }

    /**
     * 将对象转换成字符串
     * 
     * @param obj 对象
     * @return 字符串
     */
    public static String getString(Object obj) {
        return getString(obj, "");
    }

    /**
     * 将对象转换成布尔值
     * 
     * @param obj 对象
     * @param defaultValue 默认值,当传入对象为null时返回默认值
     * @return 布尔值
     */
    public static boolean getBoolean(Object obj, boolean defaultValue) {
        Boolean bool = BooleanUtils.toBooleanObject(getString(obj));
        return bool != null ? bool.booleanValue() : defaultValue;
    }

    /**
     * 将对象转换成布尔值
     * 
     * @param obj 对象
     * @return 布尔值
     */
    public static boolean getBoolean(Object obj) {
        return getBoolean(obj, false);
    }

    /**
     * 将对象转换成数字
     * 
     * @param obj 对象
     * @param defaultValue 默认值，当传入对象为null时返回默认值
     * @return 数字
     */
    public static int getInt(Object key, int defaultValue) {
        return NumberUtils.toInt(getString(key), defaultValue);
    }

    /**
     * 将对象转换成数字
     * 
     * @param obj 对象
     * @return 数字
     */
    public static int getInt(Object obj) {
        return getInt(obj, 0);
    }

    /**
     * 将对象转换成长整型
     * 
     * @param obj 对象
     * @param defaultValue 默认值，当传入对象为null时返回默认值
     * @return 长整型数字
     */
    public static long getLong(Object obj, long defaultValue) {
        return NumberUtils.toLong(getString(obj), defaultValue);
    }

    /**
     * 将对象转换成长整型
     * 
     * @param obj 对象
     * @return 长整型数字
     */
    public static long getLong(Object obj) {
        return getLong(obj, 0L);
    }

    /**
     * 将对象转换成双精度型小数
     * 
     * @param obj 对象
     * @param defaultValue 默认值，当传入对象为null时返回默认值
     * @return 双精度型小数
     */
    public static double getDouble(Object obj, double defaultValue) {
        return NumberUtils.toDouble(getString(obj), defaultValue);
    }

    /**
     * 将对象转换成双精度型小数
     * 
     * @param obj 对象
     * @return 双精度型小数
     */
    public static double getDouble(Object obj) {
        return getDouble(obj, 0d);
    }

    /**
     * 将对象转换成浮点型小数
     * 
     * @param obj 对象
     * @param defaultValue 默认值，当传入对象为null时返回默认值
     * @return 双精度型小数
     */
    public static float getFloat(Object obj, float defaultValue) {
        return NumberUtils.toFloat(getString(obj), defaultValue);
    }

    /**
     * 将对象转换成浮点型小数
     * 
     * @param obj 对象
     * @return 双精度型小数
     */
    public static float getFloat(Object obj) {
        return getFloat(obj, 0f);
    }

}
