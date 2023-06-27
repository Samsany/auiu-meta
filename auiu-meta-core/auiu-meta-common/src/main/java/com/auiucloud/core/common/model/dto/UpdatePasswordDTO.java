package com.auiucloud.core.common.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@Schema(name = "修改密码传输对象")
public class UpdatePasswordDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4448961374628943012L;

    @NotNull(message = "ID不能为空", groups = {UpdatePasswordGroup.class, SetPasswordGroup.class})
    private Long id;

    @Size(min = 6, max = 18, message = "密码长度不符合规范", groups = UpdatePasswordGroup.class)
    @NotBlank(message = "旧密码不能为空", groups = UpdatePasswordGroup.class)
    private String oldPassword;

    @Size(min = 6, max = 18, message = "密码长度不符合规范", groups = {UpdatePasswordGroup.class, SetPasswordGroup.class})
    @NotBlank(message = "新密码不能为空", groups = {UpdatePasswordGroup.class, SetPasswordGroup.class})
    private String newPassword;

    @Size(min = 6, max = 18, message = "密码长度不符合规范", groups = {UpdatePasswordGroup.class, SetPasswordGroup.class})
    @NotBlank(message = "确认密码不能为空", groups = {UpdatePasswordGroup.class, SetPasswordGroup.class})
    private String confirmPassword;

    public interface UpdatePasswordGroup {
    }

    public interface SetPasswordGroup {
    }
}
