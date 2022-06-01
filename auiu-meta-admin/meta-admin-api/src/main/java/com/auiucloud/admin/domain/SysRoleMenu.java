package com.auiucloud.admin.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色菜单关联表
 *
 * @author dries
 * @TableName sys_role_menu
 * @createDate 2022-05-31 14:59:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@TableName(value = "sys_role_menu")
@ApiModel(value = "SysRoleMenu对象", description = "角色菜单关联表")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 5033308769478889046L;

    @ApiModelProperty(value = "ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    /**
     * 菜单ID
     */
    @ApiModelProperty(value = "菜单ID")
    private Long menuId;

}
