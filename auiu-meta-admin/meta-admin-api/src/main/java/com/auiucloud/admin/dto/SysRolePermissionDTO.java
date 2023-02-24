package com.auiucloud.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * 角色权限传输对象
 *
 * @author dries
 * @createDate 2022-09-09 09-45
 */
@Data
@Schema(name = "角色权限传输对象")
public class SysRolePermissionDTO {

    @NotNull(message = "请选择角色")
    @Schema(description = "角色ID", required = true)
    private Long roleId;

    @Schema(description = "权限ID列表")
    private Set<Long> permissionIds = new HashSet<>();

}
