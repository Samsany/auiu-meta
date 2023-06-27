package com.auiucloud.core.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Context 工具类
 *
 * @author dries
 * @date 2021/12/22
 */
@Slf4j
public class SpringContextUtils implements ApplicationContextAware {

    private static final List<CallBack> CALL_BACKS = new ArrayList<>();
    public static ApplicationContext applicationContext;
    private static boolean addCallback = true;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtils.applicationContext == null) {
            SpringContextUtils.applicationContext = applicationContext;
        }
    }

    /**
     * 针对 某些初始化方法，在SpringContextHolder 未初始化时 提交回调方法。
     * 在SpringContextHolder 初始化后，进行回调使用
     *
     * @param callBack 回调函数
     */
    public synchronized static void addCallBacks(CallBack callBack) {
        if (addCallback) {
            SpringContextUtils.CALL_BACKS.add(callBack);
        } else {
            log.warn("CallBack：{} 已无法添加！立即执行", callBack.getCallBackName());
            callBack.executor();
        }
    }

    public static String getApplicationName() {
        return applicationContext.getApplicationName();
    }

    /**
     * 通过name获取 Bean
     *
     * @param name Bean名称
     * @return Bean
     */
    public static <T> T getBean(String name) {
        // noinspection unchecked
        return (T) applicationContext.getBean(name);
    }

    /**
     * 通过class获取Bean
     *
     * @param <T>   Bean类型
     * @param clazz Bean类
     * @return Bean对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param <T>   bean类型
     * @param name  Bean名称
     * @param clazz bean类型
     * @return Bean对象
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static Class<? extends Object> getType(String name) {
        return applicationContext.getType(name);
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property     属性key
     * @param defaultValue 默认值
     * @param requiredType 返回类型
     * @return /
     */
    public static <T> T getProperties(String property, T defaultValue, Class<T> requiredType) {
        T result = defaultValue;
        try {
            result = getBean(Environment.class).getProperty(property, requiredType);
        } catch (Exception ignored) {
        }
        return result;
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property 属性key
     * @return /
     */
    public static String getProperties(String property) {
        return getProperties(property, null, String.class);
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property     属性key
     * @param requiredType 返回类型
     * @return /
     */
    public static <T> T getProperties(String property, Class<T> requiredType) {
        return getProperties(property, null, requiredType);
    }

}
