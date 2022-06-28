package com.auiucloud.core.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 * @date 2021/12/20
 */
@Data
@Builder
public class LoginUser implements Serializable {
    private static final long serialVersionUID = -4757030181716930100L;

    /**
     * 用户id
     */
    @ApiModelProperty(hidden = true)
    private String userId;
    /**
     * 账号
     */
    @ApiModelProperty(hidden = true)
    private String account;
    /**
     * 用户名
     */
    @ApiModelProperty(hidden = true)
    private String name;
    /**
     * 昵称
     */
    @ApiModelProperty(hidden = true)
    private String nickname;
//    /**
//     * 租户ID
//     */
//    @ApiModelProperty(hidden = true)
//    private String tenantId;
    /**
     * 部门id
     */
    @ApiModelProperty(hidden = true)
    private String deptId;
//    /**
//     * 岗位id
//     */
//    @ApiModelProperty(hidden = true)
//    private String postId;

    /**
     * 角色id集合
     */
    @ApiModelProperty(hidden = true)
    private List<String> roles;

    /**
     * 登录类型
     */
    @ApiModelProperty(hidden = true)
    private int type;
}
