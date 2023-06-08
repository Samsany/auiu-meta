package com.auiucloud.component.cms.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SdModelVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4746263514372846455L;

    /**
     * ID
     */
    private Long id;

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
     * 融合模型列表
     */
    private List<SdFusionModelVO> fusionModelList;

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
