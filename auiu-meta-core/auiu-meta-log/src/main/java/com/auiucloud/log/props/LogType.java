package com.auiucloud.log.props;

/**
 * @author dries
 * @date 2021/12/27
 */
public enum LogType {

    /**
     * 记录日志到本地
     */
    LOGGER,
    /**
     * 记录日志到数据库
     */
    DB,
    /**
     * 记录日志到KAFKA
     */
    KAFKA,
    ;

}
