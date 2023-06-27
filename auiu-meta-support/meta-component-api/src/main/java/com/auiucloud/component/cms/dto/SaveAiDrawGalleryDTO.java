package com.auiucloud.component.cms.dto;

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
public class SaveAiDrawGalleryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -88158762636399974L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 生成数量
     */
    private Integer batchSize;

    /**
     * 图片比例
     */
    private String ratio;

    /**
     * 宽
     */
    private Integer width;

    /**
     * 高
     */
    private Integer height;

    /**
     * 消耗积分
     */
    private Integer consumeIntegral;

    /**
     * 绘画配置
     */
    private String sdConfig;

}
