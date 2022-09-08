package com.auiucloud.admin.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 系统权限表
 *
 * @author dries
 * @TableName sys_permission
 * @createDate 2022-05-31 14:59:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_permission")
@ApiModel(value = "SysPermission对象", description = "系统权限表")
public class SysPermission extends BaseEntity {

    private static final long serialVersionUID = -4466479869406663949L;

    /**
     * 菜单ID
     */
    @ApiModelProperty(value = "菜单ID")
    private Long menuId;

    /**
     * 模块名称
     */
    @ApiModelProperty(value = "模块名称")
    private String module;

    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称")
    private String name;

    /**
     * 方法类型
     */
    @ApiModelProperty(value = "方法类型")
    private String method;

    /**
     * URL权限标识
     */
    @ApiModelProperty(value = "URL权限标识")
    private String urlPerm;

    /**
     * 按钮权限标识
     */
    @ApiModelProperty(value = "按钮权限标识")
    private String btnPerm;

}
