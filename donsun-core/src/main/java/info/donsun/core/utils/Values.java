/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package info.donsun.core.utils;

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
        if (obj != null && obj.toString().length() > 0) {
            return obj.toString().trim();
        }
        return defaultValue;

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
        if (obj != null) {
            if (obj instanceof Boolean) {
                return ((Boolean) obj).booleanValue();
            } else {
                String str = obj.toString().trim();
                return "Y".equalsIgnoreCase(str) || "TRUE".equalsIgnoreCase(str) || "YES".equalsIgnoreCase(str)|| "1".equals(str);

            }
        }
        return defaultValue;
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
    public static int getInteger(Object obj, int defaultValue) {
        if (obj != null) {
            if (obj instanceof Integer) {
                return ((Integer) obj).intValue();
            } else {
                try {
                    return Integer.parseInt(obj.toString().trim());
                } catch (NumberFormatException e) {
                    // do nothing.
                }
            }
        }
        return defaultValue;
    }

    /**
     * 将对象转换成数字
     * 
     * @param obj 对象
     * @return 数字
     */
    public static int getInteger(Object obj) {
        return getInteger(obj, 0);
    }

    /**
     * 将对象转换成长整型
     * 
     * @param obj 对象
     * @param defaultValue 默认值，当传入对象为null时返回默认值
     * @return 长整型数字
     */
    public static long getLong(Object obj, long defaultValue) {
        if (obj != null) {
            if (obj instanceof Long) {
                return ((Long) obj).longValue();
            } else {
                try {
                    return Long.valueOf(obj.toString().trim());
                } catch (NumberFormatException e) {
                    // do nothing.
                }
            }
        }
        return defaultValue;
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
        if (obj != null) {
            if (obj instanceof Double) {
                return ((Double) obj).doubleValue();
            } else {
                try {
                    return Double.valueOf(obj.toString().trim());
                } catch (NumberFormatException e) {
                    // do nothing.
                }
            }
        }
        return defaultValue;
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

}
