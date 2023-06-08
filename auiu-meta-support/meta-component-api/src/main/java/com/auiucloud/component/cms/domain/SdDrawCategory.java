package com.auiucloud.component.cms.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * SD绘画类型表
 * @TableName cms_sd_draw_category
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value ="cms_sd_draw_category")
public class SdDrawCategory extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -2815603782618981200L;

    /**
     * 预览图
     */
    private String pic;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型（0-文生图）
     */
    private Integer type;

    /**
     * 状态(0-正常 1-隐藏)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 新品(0-否 1-是)
     */
    private Integer isNewModel;

    /**
     * 租户ID
     */
    private Long tenantId;

}
