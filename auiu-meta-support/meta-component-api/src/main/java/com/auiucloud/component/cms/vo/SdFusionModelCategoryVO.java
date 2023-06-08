package com.auiucloud.component.cms.vo;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * SD融合模型分类表
 * @TableName cms_sd_fusion_model_category
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SdFusionModelCategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7299140676682721171L;

    /**
     * ID
     */
    private Long id;

    /**
     * 预览图
     */
    private String pic;

    /**
     * 名称
     */
    private String name;

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
     * 备注
     */
    private String remark;

}
