package com.auiucloud.core.common.constant;

import lombok.experimental.UtilityClass;

/**
 * 常量
 *
 * @author dries
 * @date 2021/12/20
 */
@UtilityClass
public class MetaConstant {

    /**
     * 应用版本号
     */
    public final String AUIU_APP_VERSION = "1.0.0";
    /**
     * 微服务之间传递的唯一标识
     */
    public final String META_TRACE_ID = "meta-trace-id";
    /**
     * 超级管理员编码
     */
    public static final String SUPER_ADMIN_CODE = "SUPER_ADMIN";
    /**
     * 全部权限
     */
    public static final String ALL_PERMISSION = "*:*:*";
    /**
     * 日志链路追踪id日志标志
     */
    public final String LOG_TRACE_ID = "traceId";
    /**
     * 默认的路由yml配置
     */
    public static final String CONFIG_DATA_ID_DYNAMIC_ROUTES = "auiu-meta-dynamic-routes.yaml";
    /**
     * 默认分组
     */
    public static final String CONFIG_GROUP = "DEFAULT_GROUP";
    /**
     * 请求超时时长
     */
    public static final long CONFIG_TIMEOUT_MS = 5000;
    /**
     * json类型报文，UTF-8字符集
     */
    public static final String JSON_UTF8 = "application/json;charset=UTF-8";
    /**
     * 当前页码
     */
    public static final String PAGE_NUM = "pageNum";
    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "order";
}
