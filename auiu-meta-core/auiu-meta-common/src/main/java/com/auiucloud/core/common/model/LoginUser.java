package com.auiucloud.core.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(hidden = true)
    private String userId;
    /**
     * 账号
     */
    @Schema(hidden = true)
    private String account;
    /**
     * 用户名
     */
    @Schema(hidden = true)
    private String name;
    /**
     * 昵称
     */
    @Schema(hidden = true)
    private String nickname;
//    /**
//     * 租户ID
//     */
//    @Schema(hidden = true)
//    private String tenantId;
    /**
     * 部门id
     */
    @Schema(hidden = true)
    private String deptId;
//    /**
//     * 岗位id
//     */
//    @Schema(hidden = true)
//    private String postId;

    /**
     * 角色id集合
     */
    @Schema(hidden = true)
    private List<String> roles;

    /**
     * 登录类型
     */
    @Schema(hidden = true)
    private int type;
}
