package com.auiucloud.component.cms.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author dries
 **/
@Data
public class UploadAIGalleryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3368465545838247840L;

    /**
     * 编号主键标识
     */
    private Long id;

    /**
     * 原图
     */
    private String pic;

    /**
     * 画面描述
     */
    private String prompt;

    /**
     * 反面描述
     */
    private String negativePrompt;

    /**
     * 图片比例
     */
    private String ratio;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;

    /**
     * 大小
     */
    private Long size;

    /**
     * 随机种子
     */
    private Long seed;

    /**
     * 模型主题
     */
    private String model;

    /**
     * 模型风格
     */
    private String loraName;

    /**
     * 融合模型
     */
    private String fusionModel;

    /**
     * 采样迭代步数
     */
    private Integer steps;

    /**
     * 采样方法
     */
    private String sampler;

    /**
     * 提示词相关性
     */
    private Integer cfgScale;

    /**
     * 图片质量
     */
    private String picQuality;
    /**
     * 是否启用高清修复
     */
    private Integer isUp;
    /**
     * 放大倍率
     */
    private String hrUpScale;
    /**
     * 重绘幅度
     */
    private String denoisingStrength;
    /**
     * 高清修复采样次数
     */
    private Integer upSteps;
    /**
     * 放大倍率
     */
    private Integer hrScale;

    /**
     * 备注
     */
    private String remark;
}
