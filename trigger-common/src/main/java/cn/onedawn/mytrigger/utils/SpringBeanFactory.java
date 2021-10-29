package cn.onedawn.mytrigger.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author qingming yu
 * @version 1.0.0
 * @ClassName SpringBeanFactory.java
 * @Description TODO
 * @createTime 2021年10月29日 04:54:00
 */
public class SpringBeanFactory implements BeanFactoryAware, ApplicationContextAware {
    protected static BeanFactory beanFactory = null;
    private ApplicationContext applicationContext = null;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        return beanFactory.getBean(name);
    }

    public static Object getBeanByType(Class clazz) {
        return beanFactory.getBean(clazz);
    }
}
