package com.auiucloud.component.oss.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 配置表
 * @TableName sys_config
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="sys_config")
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
     * 系统内置(0-否 1-是)
     */
    private Integer configType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 租户ID
     */
    private Integer tenantId;

}
