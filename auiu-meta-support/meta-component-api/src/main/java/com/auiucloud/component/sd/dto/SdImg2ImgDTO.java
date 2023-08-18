package com.auiucloud.component.sd.dto;

import com.auiucloud.core.validator.Xss;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
public class SdImg2ImgDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2307132888132597328L;

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
     * 参考图
     */
    @NotNull(message = "请上传参考图")
    private List<String> initImages;

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
     * 启用平铺分块
     */
    private Boolean tiling = Boolean.FALSE;

    /**
     * 默认的提示词相关性 7
     */
    private Float cfgScale = 7f;

    /**
     * 生成张数
     */
    @Min(value = 1, message = "每次可生成1~4张")
    @Max(value = 4, message = "每次可生成1~4张")
    private Integer batchSize = 1;

    /**
     * 提示词
     */
    @Xss(message = "画面描述不能包含脚本字符")
    @Size(max = 2000, message = "画面描述长度在1~2000个字符之间")
    private String prompt;

    /**
     * 反向描述
     */
    @Xss(message = "反向描述不能包含脚本字符")
    @Size(max = 2000, message = "反向描述长度在0~2000个字符之间")
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
