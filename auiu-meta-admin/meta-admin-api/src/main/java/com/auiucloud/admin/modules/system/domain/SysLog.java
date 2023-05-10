package com.auiucloud.admin.modules.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志表
 *
 * @author dries
 * @TableName sys_menu
 * @createDate 2022-05-31 14:59:09
 */
@Data
@TableName(value = "sys_log")
public class SysLog implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 日志类型
     */
    private Integer type;
    /**
     * 日志标题
     */
    private String title;
    /**
     * 操作内容
     */
    private String operation;
    /**
     * 执行方法
     */
    private String method;
    /**
     * 参数
     */
    private String params;
    /**
     * 请求路径
     */
    private String url;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 异常信息
     */
    private String exception;
    /**
     * 耗时
     */
    private Long executeTime;
    /**
     * 地区
     */
    private String location;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 租户ID
     */
    private Integer tenantId;
}
