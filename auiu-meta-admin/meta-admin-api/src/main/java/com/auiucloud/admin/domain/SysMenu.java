package com.auiucloud.admin.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 系统菜单表
 *
 * @author dries
 * @TableName sys_menu
 * @createDate 2022-05-31 14:59:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_menu")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysMenu对象", description = "系统菜单表")
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = -8662478581249339874L;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 菜单名称
     */
    private String title;

    /**
     * 组件
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 重定向地址，设置为noRedirect时，面包屑不可点击
     */
    private String redirect;

    /**
     * 菜单类型(0-目录 1-菜单 2-外链)
     */
    private Integer type;

    /**
     * 菜单状态(0-禁用 1-启用)
     */
    private Integer status;

    /**
     * 永久显示根菜单(0-否 1-是)
     */
    @TableField(value = "is_always_show")
    private Integer alwaysShow;

    /**
     * 固定在tags-view中(0-否 1-是)
     */
    @TableField(value = "is_affix")
    private Integer affix;

    /**
     * 前端隐藏(0-否 1-是)
     */
    @TableField(value = "is_hidden")
    private Integer hidden;

    /**
     * 隐藏面包屑(0-否 1-是)
     */
    @TableField(value = "is_hide_header")
    private Integer hideHeader;

    /**
     * 开启缓存(0-否 1-是)
     */
    @TableField(value = "is_keep_alive")
    private Integer keepAlive;

    /**
     * 开启验证(0-否 1-是)
     */
    @TableField(value = "is_require_auth")
    private Integer requireAuth;

    /**
     * 排序
     */
    private Integer sort;

}
