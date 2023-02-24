package com.auiucloud.gen.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @authorApies
 * @createDate 2022-08-24 13-18
 */
@Data
@Schema(name = "DataSourceConnectDTO", description = "数据源连接传输对象")
public class DataSourceConnectDTO {

    /**
     * 编号
     */
    @Schema(description = "编号")
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "数据源名称不能为空")
    @Schema(description = "数据源名称", required = true)
    private String name;

    /**
     * 数据库类型
     */
    @NotBlank(message = "数据库类型不能为空")
    @Schema(description = "数据库类型", required = true)
    private String dbType;

    /**
     * 驱动类型
     */
    @Schema(description = "驱动类型")
    private String driverClass;

    /**
     * 连接地址
     */
    @Schema(description = "连接地址", required = true)
    @NotBlank(message = "连接地址不能为空")
    private String url;
    /**
     * 端口号
     */
    @Schema(description = "端口号", required = true)
    @NotBlank(message = "端口号不能为空")
    private String port;

    /**
     * 用户名
     */
    @Schema(description = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 数据库名
     */
    @Schema(description = "数据源名称", required = true)
    @NotBlank(message = "数据库名不能为空")
    private String databaseName;

    /**
     * Jdbc连接参数
     */
    @Schema(description = "Jdbc连接参数")
    private String jdbcParams;

}
