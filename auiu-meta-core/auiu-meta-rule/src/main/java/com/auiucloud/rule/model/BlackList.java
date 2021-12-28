package com.auiucloud.rule.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dries
 * @date 2021/12/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlackList {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 请求uri
     */
    private String requestUri;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 截止时间
     */
    private String endTime;

    /**
     * 黑名单状态：1:开启　0:关闭
     */
    private String status;

    /**
     * 创建时间
     */
    private String createTime;
}
