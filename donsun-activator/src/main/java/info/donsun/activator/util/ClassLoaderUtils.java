package info.donsun.activator.util;

import info.donsun.core.utils.Exceptions;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;

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
        try {
            addURL.invoke(classLoader, new Object[] { file.toURI().toURL() });
            logger.debug(file.getAbsolutePath() + " loaded.");
        } catch (Exception e) {
            logger.error("Invoke addURL method fail.", e);

        }
    }

}
