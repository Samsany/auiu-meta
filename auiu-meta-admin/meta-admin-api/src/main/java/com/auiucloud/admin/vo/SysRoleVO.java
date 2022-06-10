package com.auiucloud.admin.vo;

import com.auiucloud.admin.domain.SysRole;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author dries
 * @createDate 2022-06-08 18-00
 */
@Data
@Builder
public class SysRoleVO implements Serializable {

    private static final long serialVersionUID = -3693406136841400226L;
    public static Function<SysRole, SysRoleVO> fromRoleVO = (sysRole) -> SysRoleVO.builder()
            .roleName(sysRole.getRoleName())
            .roleCode(sysRole.getRoleCode())
            .build();
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色编码
     */
    private String roleCode;

}
