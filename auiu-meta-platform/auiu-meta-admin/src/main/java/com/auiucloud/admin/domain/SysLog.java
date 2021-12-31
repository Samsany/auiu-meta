package com.auiucloud.admin.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
* 系统日志表
* @author dries
 * @TableName sys_log
*/

@Data
@TableName("sys_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysLog对象", description = "系统日志表")
public class SysLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -359383754330589286L;

    /**
    * 日志类型
    */
    @ApiModelProperty("日志类型")
    private String type;
    /**
    * 跟踪ID
    */
    @ApiModelProperty("跟踪ID")
    private String traceId;
    /**
    * 日志标题
    */
    @ApiModelProperty("日志标题")
    private String title;
    /**
    * 操作内容
    */
    @ApiModelProperty("操作内容")
    private String operation;
    /**
    * 执行方法
    */
    @ApiModelProperty("执行方法")
    private String method;
    /**
    * 参数
    */
    @ApiModelProperty("参数")
    private String params;
    /**
    * 请求路径
    */
    @ApiModelProperty("请求路径")
    private String url;
    /**
    * ip地址
    */
    @ApiModelProperty("ip地址")
    private String ip;
    /**
    * 异常信息
    */
    @ApiModelProperty("异常信息")
    private String exception;
    /**
    * 耗时
    */
    @ApiModelProperty("耗时")
    private Long executeTime;
    /**
    * 地区
    */
    @ApiModelProperty("地区")
    private String location;

    /**
    * 租户ID
    */
    @ApiModelProperty("租户ID")
    private Integer tenantId;

}
