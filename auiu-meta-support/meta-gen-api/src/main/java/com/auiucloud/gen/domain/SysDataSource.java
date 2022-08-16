package com.auiucloud.gen.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
@ApiModel(value = "SysDataSource对象", description = "系统数据源表")
public class SysDataSource extends BaseEntity {
    private static final long serialVersionUID = 6166228667793805370L;

    /**
     * 名称
     */
    private String name;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 驱动类型
     */
    private String driverClass;

    /**
     * 连接地址
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

}
