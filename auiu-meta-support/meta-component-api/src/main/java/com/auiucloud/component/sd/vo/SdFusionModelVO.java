package com.auiucloud.component.sd.vo;

import com.auiucloud.core.database.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SdFusionModelVO  implements Serializable {

    @Serial
    private static final long serialVersionUID = -921007162721419663L;

    /**
     * ID
     */
    private Long id;

    /**
     * 所属分类ID
     */
    private Long cateId;

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
     * 备注
     */
    private String remark;

    /**
     * 是否选中
     */
    @Builder.Default
    private Boolean selected = Boolean.FALSE;

}
