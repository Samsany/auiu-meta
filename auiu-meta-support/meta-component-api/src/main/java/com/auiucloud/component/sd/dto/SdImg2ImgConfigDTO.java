package com.auiucloud.component.sd.dto;

import com.auiucloud.component.sd.domain.SdImg2ImgParams;
import com.auiucloud.component.sd.domain.SdTxt2ImgParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 生图参数
 *
 * @author dries
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SdImg2ImgConfigDTO extends SdImg2ImgParams implements Serializable {

    @Serial
    private static final long serialVersionUID = -7599166186648732229L;

    /**
     * 平台
     */
    private String platform;
    /**
     * 客户端ID
     */
    private String appId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 绘画类型
     */
    private Integer sdDrawType;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 绘画作品ID
     */
    private String drawIds;

    /**
     * 排队人数
     */
    private Integer waitQueue;

    /**
     * 消耗积分
     */
    private Integer consumeIntegral;

    /**
     * 风格
     */
    private SdImg2ImgDTO.SdStyle style;

    /**
     * 融合模型
     */
    private String lora;

    /**
     * 嵌入式
     */
    private SdImg2ImgDTO.SdEmbedding embedding;

}
