package com.auiucloud.gen.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

/**
 * 系统数据源表
 *
 * @TableName sys_data_source
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_data_source")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SysDataSource对象", description = "系统数据源表")
public class SysDataSource extends BaseEntity {
    private static final long serialVersionUID = 6166228667793805370L;

    /**
     * 名称
     */
    @NotBlank(message = "数据源名称不能为空")
    private String name;

    /**
     * 数据库类型
     */
    @NotBlank(message = "数据库类型不能为空")
    private String dbType;

    /**
     * 驱动类型
     */
    private String driverClass;

    /**
     * 连接地址
     */
    @NotBlank(message = "连接地址不能为空")
    private String url;
    /**
     * 端口号
     */
    @NotBlank(message = "端口号不能为空")
    private String port;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 数据库名
     */
    @NotBlank(message = "数据库名不能为空")
    private String databaseName;

    /**
     * Jdbc连接参数
     */
    private String jdbcParams;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

}
