package com.auiucloud.admin.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author dries
 * @createDate 2022-08-08 00-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "SysMenuDto", description = "系统菜单DTO")
public class SysMenuDto implements Serializable {
    private static final long serialVersionUID = 2559369572203501365L;

    private Long id;

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
    private Integer alwaysShow;

    /**
     * 固定在tags-view中(0-否 1-是)
     */
    private Integer affix;

    /**
     * 前端隐藏(0-否 1-是)
     */
    private Integer hidden;

    /**
     * 隐藏面包屑(0-否 1-是)
     */
    private Integer hideHeader;

    /**
     * 开启缓存(0-否 1-是)
     */
    private Integer keepAlive;

    /**
     * 开启验证(0-否 1-是)
     */
    private Integer requireAuth;

    /**
     * 排序
     */
    private Integer sort;

    private String remark;

}
