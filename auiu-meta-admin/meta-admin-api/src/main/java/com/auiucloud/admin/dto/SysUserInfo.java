package com.auiucloud.admin.dto;

import com.auiucloud.admin.domain.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 * @date 2022/4/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户信息封装")
public class SysUserInfo implements Serializable {
    private static final long serialVersionUID = 7580381700832675512L;

    /**
     * 系统用户信息
     */
    @ApiModelProperty("系统用户信息")
    private SysUser sysUser;

    /**
     * 系统权限标识组
     */
    @ApiModelProperty("系统权限标识组")
    private List<String> permissions;

    /**
     * 系统角色标识组
     */
    @ApiModelProperty(value = "系统角色标识组")
    private List<String> roles;

    /**
     * 登录类型　1：用户名密码登录　2：手机号登录　3：社交登录
     */
    @ApiModelProperty(value = "登录类型")
    private int type;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
}
