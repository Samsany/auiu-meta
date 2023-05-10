package com.auiucloud.component.sysconfig.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 配置表
 *
 * @TableName sys_config
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_config")
@Data
public class SysConfig extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 2558889661780667406L;

    /**
     * 父主键
     */
    private Long parentId;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数类型
     */
    private String configCode;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 参数类型(0-配置分类 1-配置值)
     */
    private Integer configType;

    /**
     * 是否内置(0-否 1-是)
     */
    private Integer builtIn;

    /**
     * 是否显示(0-显示 1-隐藏)
     */
    private Integer status;

    /**
     * 输入类型
     */
    private String inputType;

    /**
     * 扩展样式
     */
    private String cssClass;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 租户ID
     */
    private Integer tenantId;

}
