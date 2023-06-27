package com.auiucloud.component.sd.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
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
     * 启用脸部修复
     */
    private Boolean restoreFaces;
    /**
     * 启用高清修复
     */
    private Boolean enableHr;
    /**
     * 启用平铺分块
     */
    private Boolean tiling;

    /**
     * 默认的采样迭代步数 20
     */
    private Integer steps = 20;

    /**
     * 默认的提示词相关性 7
     */
    private Float cfgScale = 7f;

    /**
     * 生图可选的采样方式
     */
    private String sampler;

    /**
     * 生图可选的采样方式
     */
    private List<String> samplerList = new ArrayList<>();

    /**
     * 图片质量
     */
    private List<SdPicQualityVO> picQualityList;

    /**
     * 生图比例
     */
    private List<SdPicRatioVO> picRatioList;

    /**
     * 主题风格分类
     */
    private List<SdDrawStyleCategoryVO> sdDrawStyleCateList;

    /**
     * 默认主题风格
     */
    private List<SdDrawStyleVO> defaultStyleList;

    /**
     * 主题风格
     */
    private List<SdDrawStyleVO> styleList;

    /**
     * 融合模型分类
     */
    private List<SdFusionModelCategoryVO> loraCateList;

    /**
     * 生图可选的融合模型
     */
    private List<SdFusionModelVO> loraList = new ArrayList<>();

    /**
     * 生图默认的融合模型
     */
    private List<SdFusionModelVO> defaultLoraList = new ArrayList<>();

    /**
     * 生图可选的Embedding
     */
    private List<String> embeddings = new ArrayList<>();

    /**
     * 生图默认的Embedding嵌入式模型
     */
    private List<String> defaultEmbeddings = new ArrayList<>();

    /**
     * 是否选中
     */
    @Builder.Default
    private Boolean selected = Boolean.FALSE;

}
