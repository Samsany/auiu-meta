package com.auiucloud.admin.modules.system.vo;

import com.auiucloud.core.validator.Xss;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "系统用户VO")
public class SysUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 363775566960434991L;

    @Schema(description = "序号")
    private Long id;

    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账户不能为空")
    @Size(min = 2, max = 30, message = "用户账号长度在2~30个字符之间")
    @Schema(description = "账号")
    private String account;

    @JsonIgnore
    @Schema(description = "密码")
    private String password;

    @NotNull(message = "请授权用户角色")
    @Schema(description = "用户角色")
    private List<SysRoleVO> roles;

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

    @Schema(description = "状态")
    private Integer status;

    /**
     * 账户是否过期(0-过期 1-未过期)
     */
    @Schema(description = "账户是否过期")
    private boolean accountNonExpired;

    /**
     * 账户是否锁定(0-锁定 1-未锁定)
     */
    @Schema(description = "账户是否锁定")
    private boolean accountNonLocked;

    /**
     * 证书(密码)是否过期(0-过期 1-未过期)
     */
    @Schema(description = "证书(密码)是否过期")
    private boolean credentialsNonExpired;

    @Schema(description = "内置用户")
    private boolean builtIn;

    @Schema(description = "最后登录时间")
    private Date loginDate;

    @Schema(description = "登录ip")
    private String loginIp;

    @Schema(description = "注册ip")
    private String registerIp;

    @Schema(description = "注册地址")
    private String registerAddress;

    @Schema(description = "注册方式")
    private String registerSource;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "备注")
    private String remark;

}
