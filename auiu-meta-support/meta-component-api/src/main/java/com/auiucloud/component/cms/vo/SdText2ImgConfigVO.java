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
public class SdText2ImgConfigVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7030813254212362745L;

    /**
     * 生图比例
     */
    private List<PicRatioVO> picRatioList;

    /**
     * 图片质量
     */
    private List<PicQualityVO> sdUpScaleList;

    /**
     * 模型
     */
    private List<SdModelVO> sdModelList;

    /**
     * 融合模型分类
     */
    private List<SdFusionModelCategoryVO> fusionModelCateList;

    /**
     * 主题风格分类
     */
    private List<SdDrawStyleCategoryVO> sdDrawStyleCateList;

    /**
     * 主题风格
     */
    private List<SdDrawStyleVO> sdDrawStyleList;


}
