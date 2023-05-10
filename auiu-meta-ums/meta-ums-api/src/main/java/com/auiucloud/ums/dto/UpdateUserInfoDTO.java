package com.auiucloud.ums.dto;

import com.auiucloud.core.validator.Xss;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "用户信息编辑对象")
public class UpdateUserInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3271458482194490397L;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "背景")
    private String bgImg;

    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "性别")
    private Integer gender;

    @Xss(message = "用户简介不能包含脚本字符")
    @Size(min = 0, max = 500, message = "用户简介长度不能超过30个字符")
    @Schema(description = "个人简介")
    private String remark;

}
