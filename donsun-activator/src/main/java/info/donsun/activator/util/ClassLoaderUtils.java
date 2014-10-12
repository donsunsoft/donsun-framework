/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package info.donsun.activator.util;

import info.donsun.activator.common.Constants;
import info.donsun.core.utils.Exceptions;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;

/**
 * 类加载工具
 * 
 * @author Steven.Deng
 * @date 2014年3月5日
 */
public class ClassLoaderUtils {

    private static Logger logger = Logger.getLogger(ClassLoaderUtils.class);

    /** URLClassLoader的addURL方法 */
    private static Method addURL = initAddMethod();

    /** 初始化方法 */
    private static final Method initAddMethod() {
        try {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            add.setAccessible(true);
            return add;
        } catch (Exception e) {
            logger.error("Init addMethod fail.", e);
            Exceptions.unchecked(e);
        }
        return null;
    }

    /**
     * 加载jar文件
     * 
     * @param file
     * @param classLoader
     */
    public static void loadJarFile(File file, ClassLoader classLoader) {
        if (file.exists()) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(Constants.ENTRY_SUFFIEX)) {
                try {
                    loadJarFile(file.toURI().toURL(), classLoader);
                } catch (MalformedURLException e) {
                    logger.error("Invoke addURL method fail.", e);
                }
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    loadJarFile(f, classLoader);
                }
            }
        }
    }

    /**
     * 加载jar文件
     * 
     * @param url
     * @param classLoader
     */
    public static void loadJarFile(URL url, ClassLoader classLoader) {
        if (LoaderUtils.isJarURL(url)) {
            try {
                addURL.invoke(classLoader, new Object[] { url });
                logger.debug(url.toString() + " loaded.");
            } catch (Exception e) {
                logger.error("Invoke addURL method fail.", e);

            }
        }
    }

}
