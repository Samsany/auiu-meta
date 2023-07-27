package com.auiucloud.component.sd.vo;

import com.auiucloud.component.sd.domain.SdModelConfig;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dries
 **/
@Data
public class SdModelConfigVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3620576398033501544L;

    /**
     * id
     */
    private Long id;

    /**
     * 所属分类ID
     */
    @NotNull(message = "请选择模型分类")
    private Long cateId;

    /**
     * 名称
     */
    @NotBlank(message = "请输入模型名称")
    private String name;

    /**
     * 预览图
     */
    @NotBlank(message = "请上传模型缩略图")
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
    private String filePath;

    /**
     * 文件名
     */
    @NotBlank(message = "请输入模型文件名")
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
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

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

    // /**
    //  * 生图默认的Embedding嵌入式模型
    //  */
    // private List<String> defaultNeEmbeddings = new ArrayList<>();

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
