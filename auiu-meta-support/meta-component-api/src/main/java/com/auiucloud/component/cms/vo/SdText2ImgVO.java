package com.auiucloud.component.cms.vo;

import com.auiucloud.component.cms.domain.SdText2ImgParam;
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
public class SdText2ImgVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 239573060741678614L;

    /**
     * SD 绘图配置
     */
    private SdText2ImgParam text2ImgParam;

    /**
     * 图片质量ID
     */
    private Long sdUpScaleId;

    /**
     * 模型ID
     */
    private Long sdModelId;

    /**
     * 生图比例ID
     */
    private Long ratioId;

    /**
     * 生图数量
     */
    private Integer picQuantity;


}
