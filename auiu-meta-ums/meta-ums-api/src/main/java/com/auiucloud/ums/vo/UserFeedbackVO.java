package com.auiucloud.ums.vo;

import com.auiucloud.core.validator.Xss;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFeedbackVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6247469566911814480L;

    private Long userId;

    private String nickname;

    private String avatar;

    /**
     * 联系方式
     */
    @Xss(message = "联系方式不能包含脚本字符")
    @Size(max = 50, message = "联系方式长度在0~50个字符之间")
    private String contactInformation;

    /**
     * 反馈类型
     */
    private String feedType;

    /**
     * 反馈内容
     */
    @NotBlank(message = "请填写反馈内容")
    @Xss(message = "反馈内容不能包含脚本字符")
    @Size(max = 1024, message = "反馈内容长度在0~1000个字符之间")
    private String content;

    /**
     * 状态(0-已提交 1-已回复)
     */
    @Builder.Default
    private Integer status = 0;

    /**
     * 图片地址列表
     */
    @Size(max = 1024, message = "图片链接长度在0~1000字符之间")
    private String picUrls;
}
