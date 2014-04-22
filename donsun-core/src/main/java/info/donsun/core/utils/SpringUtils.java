package info.donsun.core.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.StringUtils;

/**
 * Spring ToolKit
 * 
 * @author Steven
 * 
 */
public class SpringUtils {

    public static <T> T createBean(DefaultListableBeanFactory beanFactory, Class<T> beanClass, Object... constructorArgs) {
        return createBean(beanFactory, beanClass, Arrays.asList(constructorArgs));
    }

    public static <T> T createBean(DefaultListableBeanFactory beanFactory, Class<T> beanClass, Map<String, Object> properties) {
        return createBean(beanFactory, beanClass, null, properties);
    }

    public static <T> T createBean(DefaultListableBeanFactory beanFactory, Class<T> beanClass, List<Object> constructorArgs) {
        return createBean(beanFactory, beanClass, constructorArgs, null);
    }

    public static <T> T createBean(DefaultListableBeanFactory beanFactory, Class<T> beanClass, List<Object> constructorArgs, Map<String, Object> properties) {
        return createBean(beanFactory, beanClass, constructorArgs, properties, StringUtils.uncapitalize(beanClass.getSimpleName()));
    }

    public static <T> T createBean(DefaultListableBeanFactory beanFactory, Class<T> beanClass, List<Object> constructorArgs, Map<String, Object> properties, String beanName) {
        return createBean(beanFactory, beanClass, constructorArgs, properties, beanName, true);
    }

    public static <T> T createBean(DefaultListableBeanFactory beanFactory, Class<T> beanClass, List<Object> constructorArgs, Map<String, Object> properties, String beanName,
            boolean singleton) {
        BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass).setScope(
                singleton ? BeanDefinition.SCOPE_SINGLETON : BeanDefinition.SCOPE_PROTOTYPE);
        if (constructorArgs != null && constructorArgs.size() > 0) {
            for (Object arg : constructorArgs) {
                if (arg instanceof String && beanFactory.containsBean((String) arg)) {
                    beanBuilder.addConstructorArgReference((String) arg);
                } else {
                    beanBuilder.addConstructorArgValue(arg);
                }
            }
        }
        if (properties != null && properties.size() > 0) {
            for (Entry<String, Object> entry : properties.entrySet()) {
                if (entry.getValue() instanceof String && beanFactory.containsBean((String) entry.getValue())) {
                    beanBuilder.addPropertyReference(entry.getKey(), (String) entry.getValue());
                } else {
                    beanBuilder.addPropertyValue(entry.getKey(), entry.getValue());
                }
            }
        }
        beanFactory.registerBeanDefinition(beanName, beanBuilder.getRawBeanDefinition());
        return beanFactory.getBean(beanName, beanClass);
    }
}
