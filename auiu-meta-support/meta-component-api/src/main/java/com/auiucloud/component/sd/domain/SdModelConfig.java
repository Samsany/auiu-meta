package com.auiucloud.component.sd.domain;

import com.auiucloud.component.sd.vo.SdLoraVO;
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
public class SdModelConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 2372984612160080797L;

    /**
     * 启用脸部修复
     */
    private Integer restoreFaces;
    /**
     * 启用高清修复
     */
    private Integer enableHr;
    /**
     * 启用平铺分块
     */
    private Integer tiling;

    /**
     * 生图质量关联
     */
    private List<Long> picQualities = new ArrayList<>();

    /**
     * 生图比例关联
     */
    private List<Long> picRatios = new ArrayList<>();

    /**
     * 生图可选风格关联
     */
    private List<Long> styles = new ArrayList<>();

    /**
     * 生图默认风格关联
     */
    private List<Long> defaultStyles = new ArrayList<>();

    /**
     * 生图可选的融合模型
     */
    private List<SdLoraVO> loraList = new ArrayList<>();

    /**
     * 生图默认的融合模型
     */
    private List<SdLoraVO> defaultLoraList = new ArrayList<>();

    /**
     * 生图可选的Embedding
     */
    private List<String> embeddings = new ArrayList<>();

    /**
     * 生图默认的Embedding嵌入式模型
     */
    private List<String> defaultEmbeddings = new ArrayList<>();

    /**
     * 生图可选的采样方式
     */
    private String defaultSampler;

    /**
     * 生图可选的采样方式
     */
    private List<String> samplerList = new ArrayList<>();

    /**
     * 默认的采样迭代步数 20
     */
    private Integer steps = 20;

    /**
     * 默认的提示词相关性 7
     */
    private Float cfgScale = 7f;

}
