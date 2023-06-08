package com.auiucloud.component.cms.vo;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * SD绘画风格分类表
 * @TableName cms_sd_draw_style_category
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SdDrawStyleCategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6292028402037047238L;

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
