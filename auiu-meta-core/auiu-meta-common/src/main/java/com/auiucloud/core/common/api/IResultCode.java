package com.auiucloud.core.common.api;

/**
 * @author dries
 * @date 2021/12/21
 */
public interface IResultCode {

    /**
     * 获取code
     *
     * @return int
     */
    int getCode();

    /**
     * 获取消息
     *
     * @return string
     */
    String getMessage();

}
