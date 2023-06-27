package com.auiucloud.component.sd.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * SD图片放大算法表
 * @TableName sd_up_scale
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SdPicQualityVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2586207604926356882L;

    /**
     * ID
     */
    private Long id;

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
     * 模型路径
     */
    private String modelPath;

    /**
     * 模型地址
     */
    private String modelUrl;

    /**
     * 启用高清修复(0-否 1-是)
     */
    private Integer enableHr;

    /**
     * 缩放比例
     */
    private Float hrScale;

    /**
     * 最大缩放比例
     */
    private Float maxHrScale;

    /**
     * 重绘幅度
     */
    private Double denoisingStrength;

    /**
     * 高清修复采样步数
     */
    private Integer steps;

    /**
     * 调整宽度
     */
    private Integer hrResizeX;

    /**
     * 调整高度
     */
    private Integer hrResizeY;

    /**
     * 消耗积分
     */
    private Integer consumeIntegral;

    /**
     * 状态(0-正常 1-隐藏)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 是否选中
     */
    @Builder.Default
    private Boolean selected = Boolean.FALSE;
}
