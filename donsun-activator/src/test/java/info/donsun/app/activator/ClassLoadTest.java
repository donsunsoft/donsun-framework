package info.donsun.app.activator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ClassLoadTest {

    @Test
    public void test1() {
        String path = "modules/msl/commons-dbcp-1.3.jar";// 外部jar包的路径
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();// 所有的Class对象
        Map<Class<?>, Annotation[]> classAnnotationMap = new HashMap<Class<?>, Annotation[]>();// 每个Class对象上的注释对象
        Map<Class<?>, Map<Method, Annotation[]>> classMethodAnnoMap = new HashMap<Class<?>, Map<Method, Annotation[]>>();// 每个Class对象中每个方法上的注释对象
        try {
            JarFile jarFile = new JarFile(new File(path));
            URL url = new URL("file:" + path);
            ClassLoader loader = new URLClassLoader(new URL[] { url });// 自己定义的classLoader类，把外部路径也加到load路径里，使系统去该路经load对象
            Enumeration<JarEntry> es = jarFile.entries();
            while (es.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) es.nextElement();
                String name = jarEntry.getName();
                if (name != null && name.endsWith(".class")) {// 只解析了.class文件，没有解析里面的jar包
                    // 默认去系统已经定义的路径查找对象，针对外部jar包不能用
                    // Class<?> c =
                    // Thread.currentThread().getContextClassLoader().loadClass(name.replace("/",
                    // ".").substring(0,name.length() - 6));
                    Class<?> c = loader.loadClass(name.replace("/", ".").substring(0, name.length() - 6));// 自己定义的loader路径可以找到
                    System.out.println(c);
                    classes.add(c);
                    Annotation[] classAnnos = c.getDeclaredAnnotations();
                    classAnnotationMap.put(c, classAnnos);
                    Method[] classMethods = c.getDeclaredMethods();
                    Map<Method, Annotation[]> methodAnnoMap = new HashMap<Method, Annotation[]>();
                    for (int i = 0; i < classMethods.length; i++) {
                        Annotation[] a = classMethods[i].getDeclaredAnnotations();
                        methodAnnoMap.put(classMethods[i], a);
                    }
                    classMethodAnnoMap.put(c, methodAnnoMap);
                }
            }
            System.out.println(classes.size());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        String path = "modules/msl/commons-dbcp-1.3.jar";// 此jar包里还有别的jar包
        try {
            JarFile jarfile = new JarFile(new File(path));
            Enumeration<JarEntry> es = jarfile.entries();
            while (es.hasMoreElements()) {
                JarEntry je = es.nextElement();
                String name = je.getName();
                if (name.endsWith(".jar")) {// 读取jar包里的jar包
                    File f = new File(name);
                    JarFile j = new JarFile(f);
                    Enumeration<JarEntry> e = j.entries();
                    while (e.hasMoreElements()) {
                        JarEntry jarEntry = (JarEntry) e.nextElement();
                        System.out.println(jarEntry.getName());
                        // .........接下去和上面的方法类似
                    }
                }
                // System.out.println(je.getName());
                if (je.getName().equals("lang.properties")) {
                    InputStream inputStream = jarfile.getInputStream(je);
                    Properties properties = new Properties();
                    properties.load(inputStream);
                    Iterator<Object> ite = properties.keySet().iterator();
                    while (ite.hasNext()) {
                        Object key = ite.next();
                        System.out.println(key + " : " + properties.get(key));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
