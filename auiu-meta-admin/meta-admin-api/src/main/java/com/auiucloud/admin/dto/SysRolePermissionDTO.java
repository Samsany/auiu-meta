package com.auiucloud.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "角色权限传输对象")
public class SysRolePermissionDTO {

    @NotNull(message = "请选择角色")
    @ApiModelProperty(value = "角色ID", required = true)
    private Long roleId;

    @ApiModelProperty(value = "权限ID列表")
    private Set<Long> permissionIds = new HashSet<>();

}
