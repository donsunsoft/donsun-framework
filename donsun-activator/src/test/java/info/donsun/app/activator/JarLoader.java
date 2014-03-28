package info.donsun.app.activator;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarLoader {
    private URLClassLoader urlClassLoader;

    public JarLoader(URLClassLoader urlClassLoader) {
        this.urlClassLoader = urlClassLoader;
    }

    public void loadJar(URL url) throws Exception {
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        addURL.invoke(urlClassLoader, url);
    }

    public Class<?> findClass(String className) {
        try {
            return urlClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            System.out.println("  not exist!");
        }
        return null;
    }

    private static void loadjar(JarLoader jarLoader, String path) throws MalformedURLException, Exception {
        File libdir = new File(path);
        if (libdir != null && libdir.isDirectory()) {

            File[] listFiles = libdir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    // TODO Auto-generated method stub
                    return file.exists() && file.isFile() && file.getName().endsWith(".jar");
                }
            });

            for (File file : listFiles) {
                jarLoader.loadJar(file.toURI().toURL());
            }

        } else {
            System.out.println("[Console Message] Directory [" + path + "] does not exsit, please check it");
            System.exit(0);
        }
    }

    public static void main(String[] args) throws MalformedURLException, Exception {
        JarLoader jarLoader = new JarLoader((URLClassLoader) ClassLoader.getSystemClassLoader());

        loadjar(jarLoader, "modules/msl");
        
        Class<?> clazz = jarLoader.findClass("foo.Test");
        Object obj;
        try {
            obj = clazz.newInstance();
            Object result = clazz.getMethod("test", String.class).invoke(obj, "fdsa");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}