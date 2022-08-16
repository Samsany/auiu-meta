package com.auiucloud.core.common.constant;

/**
 * 通用常量
 *
 * @author dries
 * @createDate 2022-06-08 16-55
 */
public class CommonConstant {

    public static final Long ROOT_NODE_ID = 0L; // 根节点

    public static final Long NODE_ONE_ID = 1L; // 节点一

    /**
     * 禁用状态
     */
    public static final int STATUS_DISABLE_VALUE = 0;
    /**
     * 启用状态
     */
    public static final int STATUS_NORMAL_VALUE = 1;

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

    /**
     * LDAP 远程方法调用
     */
    public static final String LDAP_LOGIN_TYPE = "ldap";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * json类型报文，UTF-8字符集
     */
    public static final String JSON_UTF8 = "application/json;charset=UTF-8";
}
