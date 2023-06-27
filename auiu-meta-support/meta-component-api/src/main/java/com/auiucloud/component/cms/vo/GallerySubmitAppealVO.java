package com.auiucloud.component.cms.vo;

import com.auiucloud.core.validator.Xss;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "提交作品申诉VO")
public class GallerySubmitAppealVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 9115395963856349727L;

    @NotNull(message = "用户不存在")
    private Long userId;

    @NotNull(message = "作品不存在")
    private Long galleryId;

    @Xss(message = "申诉内容不能包含脚本字符")
    @Size(min = 2, max = 200, message = "申诉内容长度在0~200个字符之间")
    private String reason;
}
