package com.auiucloud.admin.modules.system.dto;

import com.auiucloud.admin.modules.system.vo.SysDeptVO;
import com.auiucloud.core.validator.group.InsertGroup;
import com.auiucloud.core.validator.group.UpdateGroup;
import com.auiucloud.core.validator.Xss;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "系统用户传输对象DTO")
public class SysUserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1657540613094773636L;

    @NotNull(message = "用户ID不能为空", groups = {UpdateGroup.class})
    @Schema(description = "序号")
    private Long id;

    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账户不能为空")
    @Size(min = 2, max = 30, message = "用户账号长度在2~30个字符之间")
    @Schema(description = "账号")
    private String account;

    @NotBlank(message = "用户账户不能为空", groups = {InsertGroup.class})
    @Schema(description = "密码")
    private String password;

    @NotNull(message = "请授权用户角色")
    @Schema(description = "用户角色")
    private List<Long> roleIds;

    @NotNull(message = "请选择用户归属部门")
    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "归属部门")
    private SysDeptVO depart;

    @Xss(message = "用户姓名不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户姓名长度不能超过30个字符")
    @Schema(description = "姓名")
    private String realName;

    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    @Schema(description = "手机号")
    private String mobile;

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "出生日期")
    private Date birthday;

    @Schema(description = "注册ip")
    private String registerIp;

    @Schema(description = "注册地址")
    private String registerAddress;

    @Schema(description = "注册方式")
    private String registerSource;

    @Schema(description = "备注")
    private String remark;
}
