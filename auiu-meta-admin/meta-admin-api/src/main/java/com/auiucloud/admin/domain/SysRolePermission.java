package com.auiucloud.admin.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色权限关联表
 *
 * @author dries
 * @TableName sys_role_permission
 * @createDate 2022-05-31 14:59:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@TableName(value = "sys_role_permission")
@Schema(name = "SysRolePermission对象", description = "角色权限关联表")
public class SysRolePermission implements Serializable {

    private static final long serialVersionUID = -574814089772085960L;

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long roleId;

    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    private Long permId;

}
