package com.auiucloud.admin.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 菜单视图对象
 *
 * @author dries
 * @createDate 2022-06-08 16-01
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenuVO {
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
     * 永久显示根菜单(0-否 1-是)
     */
    @TableField(value = "is_always_show")
    private boolean alwaysShow;

    /**
     * 固定在tags-view中(0-否 1-是)
     */
    @TableField(value = "is_affix")
    private boolean affix;

    /**
     * 前端隐藏(0-否 1-是)
     */
    @TableField(value = "is_hide_header")
    private boolean hidden;

    /**
     * 隐藏面包屑(0-否 1-是)
     */
    @TableField(value = "is_hide_header")
    private boolean hideHeader;

    /**
     * 开启缓存(0-否 1-是)
     */
    @TableField(value = "is_keep_alive")
    private boolean keepAlive;

    /**
     * 开启验证(0-否 1-是)
     */
    @TableField(value = "is_require_auth")
    private boolean requireAuth;

    /**
     * 排序
     */
    private Integer sort;


}
