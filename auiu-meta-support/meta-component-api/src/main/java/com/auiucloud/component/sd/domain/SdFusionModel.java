package com.auiucloud.component.sd.domain;

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
 * SD融合模型表
 * @TableName sd_fusion_model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value ="sd_fusion_model")
public class SdFusionModel extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -921007162721419663L;

    /**
     * 预览图
     */
    private String pic;

    /**
     * 标题
     */
    private String title;

    /**
     * 名称
     */
    private String name;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 默认值
     */
    private Double defaultValue;

    /**
     * 画面描述
     */
    private String prompt;

    /**
     * 反向描述
     */
    private String negativePrompt;

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

    /**
     * 所属分类ID
     */
    private Long cateId;

}
