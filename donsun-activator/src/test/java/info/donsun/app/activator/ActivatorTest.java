package info.donsun.app.activator;

import info.donsun.activator.ApplicationLoader;
import info.donsun.activator.model.Application;
import info.donsun.core.utils.Reflections;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = { "classpath:spring/spring-context.xml",
// "classpath:spring/spring-shiro.xml" })
@ContextConfiguration(locations = { "classpath:spring-config.xml" })
@ActiveProfiles("dev")
public class ActivatorTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private ApplicationLoader applicationLoader;

    @Test
    public void testLoader() throws IOException, SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException,
            InvocationTargetException, ClassNotFoundException {

        String msg = applicationContext.getMessage("who.am.i", null, Locale.getDefault());

        System.out.println(msg);
        // applicationContext.getMessage(code, args, defaultMessage, locale)

        Object obj = applicationContext.getBean("testBean");
        
        Application app= applicationLoader.getApplication();
        String m = app.getProperty("main.class");
        String g = app.getProperty("execute.method");
        
        System.out.println(Reflections.invokeMethodByName(obj, "go", null));

        
        Reflections.invokeMethodByName(obj, "main", new Object[] { new String[] { } });

        // tryInvoke(Class.forName("foo.Dada"));

    }

}
