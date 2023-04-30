package com.auiucloud.component.cms.vo;

import com.auiucloud.core.validator.Xss;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
public class GalleryPublishVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5321807155795881686L;

    @NotNull(message = "作品不存在")
    private Long id;

    /**
     * 标题
     */
    @NotBlank(message = "作品名称不能为空")
    @Xss(message = "作品名称不能包含脚本字符")
    @NotBlank(message = "作品名称不能为空")
    @Size(min = 0, max = 100, message = "作品名称长度在2~30个字符之间")
    private String title;

    /**
     * 备注
     */
    @Size(min = 0, max = 500, message = "作品简介长度在0~1200字符之间")
    private String remark;

    /**
     * 画面描述
     */
    @Size(min = 0, max = 1200, message = "作品简介长度在0~1200字符之间")
    private String prompt;

    /**
     * 标签ID
     */
    private Long tagId;
}
