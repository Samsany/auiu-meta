package com.auiucloud.admin.modules.system.vo;

import com.auiucloud.admin.modules.system.domain.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 * @date 2022/4/11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "用户登录信息VO")
public class UserInfoVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7580381700832675512L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 系统用户信息
     */
    @Schema(description = "系统用户信息")
    private SysUser sysUser;

    /**
     * 系统角色标识组
     */
    @Schema(description = "系统角色标识组")
    private List<String> roles;

    /**
     * 系统权限标识组
     */
    @Schema(description = "系统权限标识组")
    private List<String> permissions;

    /**
     * 登录类型　1：用户名密码登录　2：手机号登录　3：社交登录
     */
    @Schema(description = "登录类型")
    private String loginType;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private String tenantId;
}
