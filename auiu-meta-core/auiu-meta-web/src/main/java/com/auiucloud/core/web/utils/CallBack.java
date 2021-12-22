package com.auiucloud.core.web.utils;

/**
 * 回调方法
 * 针对某些初始化方法，在SpringContextUtils初始化前时，可提交一个回调任务
 * 在SpringContextUtils初始化后，进行回调使用
 *
 * @author dries
 * @date 2021/12/22
 */
public interface CallBack {

    /**
     * 回调执行方法
     */
    void executor();

    /**
     * 本回调任务名称
     *
     * @return /
     */
    default String getCallBackName() {
        return Thread.currentThread().getId() + ":" + this.getClass().getName();
    }

}
