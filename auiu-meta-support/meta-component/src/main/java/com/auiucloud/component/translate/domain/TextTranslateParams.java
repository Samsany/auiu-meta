package com.auiucloud.component.translate.domain;

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
 * 通用文本翻译
 *
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextTranslateParams implements Serializable {
    @Serial
    private static final long serialVersionUID = 7756413733486411358L;

    /**
     * 翻译源语言 可设置为auto
     */
    @Size(min = 1, max = 100, message = "源语言长度在1~100个字符之间")
    @Xss(message = "内容不能包含脚本字符")
    @NotBlank(message = "翻译源语言不能为空")
    private String from;

    /**
     * 翻译目标语言
     */
    @Size(min = 1, max = 100, message = "目标语言长度在1~100个字符之间")
    @Xss(message = "内容不能包含脚本字符")
    @NotBlank(message = "翻译目标语言不能为空")
    private String to;

    /**
     * 翻译内容
     */
    @Xss(message = "内容不能包含脚本字符")
    @Size(min = 1, max = 2000, message = "内容长度在2~1000个字符之间")
    @NotBlank(message = "请输入画面描述")
    private String content;

}
