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
 * SD模型主题表
 * @TableName cms_sd_model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value ="cms_sd_model")
public class SdModel extends BaseEntity {


    @Serial
    private static final long serialVersionUID = 2817725562873770534L;

    /**
     * 所属分类ID
     */
    private Long cateId;

    /**
     * 名称
     */
    private String name;

    /**
     * 预览图
     */
    private String pic;

    /**
     * 标题
     */
    private String title;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 模型HASH值
     */
    private String modelHash;

    /**
     * sha256
     */
    private String sha256;

    /**
     * 文件路径
     */
    private String filename;

    /**
     * 配置
     */
    private String config;

    /**
     * 融合模型
     */
    private String fusionModal;

    /**
     * 状态(0-正常 1-隐藏)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 热门(0-否 1-是)
     */
    private Integer isHotModel;

    /**
     * 新品(0-否 1-是)
     */
    private Integer isNewModel;

    /**
     * 租户ID
     */
    private Long tenantId;

}
