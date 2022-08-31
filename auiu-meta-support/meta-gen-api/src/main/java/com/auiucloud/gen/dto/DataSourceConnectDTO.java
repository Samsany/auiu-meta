package com.auiucloud.gen.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @authorApies
 * @createDate 2022-08-24 13-18
 */
@Data
@ApiModel(value = "DataSourceConnectDTO", description = "数据源连接传输对象")
public class DataSourceConnectDTO {

    /**
     * 名称
     */
    @NotBlank(message = "数据源名称不能为空")
    @ApiModelProperty(value = "数据源名称", required = true)
    private String name;

    /**
     * 数据库类型
     */
    @NotBlank(message = "数据库类型不能为空")
    @ApiModelProperty(value = "数据库类型", required = true)
    private String dbType;

    /**
     * 驱动类型
     */
    @ApiModelProperty(value = "驱动类型")
    private String driverClass;

    /**
     * 连接地址
     */
    @ApiModelProperty(value = "连接地址", required = true)
    @NotBlank(message = "连接地址不能为空")
    private String url;
    /**
     * 端口号
     */
    @ApiModelProperty(value = "端口号", required = true)
    @NotBlank(message = "端口号不能为空")
    private String port;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 数据库名
     */
    @ApiModelProperty(value = "数据源名称", required = true)
    @NotBlank(message = "数据库名不能为空")
    private String databaseName;

    /**
     * Jdbc连接参数
     */
    @ApiModelProperty(value = "Jdbc连接参数")
    private String jdbcParams;

}
