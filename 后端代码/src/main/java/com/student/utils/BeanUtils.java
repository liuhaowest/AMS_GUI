package com.student.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 李123
 */
@Component
public class BeanUtils implements ApplicationContextAware {
    private static ApplicationContext context;

    // springboot加载完成后会把beanfactory作为参数传给次方法，然后我们可以把工厂赋值给context。
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
    // 通过context获取bean
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }
}
