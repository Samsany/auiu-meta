package com.auiucloud.admin.modules.system.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "SysPermission对象", description = "系统权限表")
public class SysPermission extends BaseEntity {

    private static final long serialVersionUID = -4466479869406663949L;

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    private Long menuId;

    /**
     * 模块名称
     */
    @Schema(description = "模块名称")
    private String module;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    private String name;

    /**
     * 方法类型
     */
    @Schema(description = "方法类型")
    private String method;

    /**
     * URL权限标识
     */
    @Schema(description = "URL权限标识")
    private String urlPerm;

    /**
     * 按钮权限标识
     */
    @Schema(description = "按钮权限标识")
    private String btnPerm;

}
