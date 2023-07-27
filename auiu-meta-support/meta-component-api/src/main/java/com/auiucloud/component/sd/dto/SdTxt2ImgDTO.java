package com.auiucloud.component.sd.dto;

import com.auiucloud.core.validator.Xss;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * 接收生图参数
 *
 * @author dries
 **/
@Data
public class SdTxt2ImgDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1993365554529220271L;

    /**
     * 任务标识
     */
    @NotBlank(message = "任务标识获取异常")
    private String taskId;

    /**
     * 客户端ID
     * 平台
     */
    @NotBlank(message = "平台解析异常")
    private String platform;

    /**
     * 模型ID
     */
    @NotNull(message = "请选择模型")
    private Long modelId;

    /**
     * 用户ID
     */
    @NotNull(message = "用户未登录或登录已过期")
    private Long userId;

    /**
     * 启用脸部修复
     */
    private Boolean restoreFaces = Boolean.FALSE;
    /**
     * 启用高清修复
     */
    private Boolean enableHr = Boolean.FALSE;
    /**
     * 启用平铺分块
     */
    private Boolean tiling = Boolean.FALSE;

    /**
     * 默认的提示词相关性 7
     */
    private Float cfgScale = 7f;

    /**
     * 图片比例ID
     */
    @NotNull(message = "请选择画面比例")
    private Long picRatioId;

    /**
     * 图片质量ID
     */
    @NotNull(message = "请选择画面质量")
    private Long picQualityId;

    /**
     * 生成张数
     */
    @NotNull(message = "请选择作图数量")
    @Min(value = 1, message = "每次可生成1~4张")
    @Max(value = 4, message = "每次可生成1~4张")
    private Integer batchSize = 1;

    /**
     * 提示词
     */
    @Xss(message = "画面描述不能包含脚本字符")
    @Size(min = 1, max = 2000, message = "画面描述长度在1~2000个字符之间")
    @NotBlank(message = "请输入画面描述")
    private String prompt;

    /**
     * 反向描述
     */
    @Xss(message = "反向描述不能包含脚本字符")
    @Size(min = 0, max = 2000, message = "反向描述长度在0~2000个字符之间")
    private String negativePrompt;

    /**
     * 采样方法
     */
    @Xss(message = "采样方法不能包含脚本字符")
    private String samplerName = "Euler a";

    /**
     * 采样迭代步数
     */
    @Min(value = 1, message = "采样迭代步数取值在1~150之间")
    @Max(value = 150, message = "采样迭代步数取值在1~150之间")
    private Integer steps = 30;

    /**
     * 风格
     */
    private SdStyle style;

    /**
     * 融合模型
     */
    private String lora;

    /**
     * 嵌入式
     */
    private SdEmbedding embedding;

    @Data
    public static class SdStyle {
        /**
         * 提示词
         */
        private String prompt = "";

        /**
         * 反向描述
         */
        private String negativePrompt = "";
    }

    @Data
    public static class SdEmbedding {
        /**
         * 提示词
         */
        private String prompt = "";

        /**
         * 反向描述
         */
        private String negativePrompt = "";
    }

    private String info;
}
