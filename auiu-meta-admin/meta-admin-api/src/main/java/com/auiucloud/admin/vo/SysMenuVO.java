package com.auiucloud.admin.vo;

import com.auiucloud.core.common.tree.INode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单视图对象
 *
 * @author dries
 * @createDate 2022-06-08 16-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenuVO implements INode {
    private static final long serialVersionUID = -8662478581249339874L;

    /**
     * ID
     */
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
     * 路由参数
     */
    private String queryParams;

    /**
     * 重定向地址，设置为noRedirect时，面包屑不可点击
     */
    private String redirect;

    /**
     * 是否内嵌(0-否 1-是)
     */
    private Integer iframe;

    /**
     * 内链地址
     */
    private String iframeSrc;

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
    private Integer keepalive;

    /**
     * 开启验证(0-否 1-是)
     */
    private Integer requireAuth;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 子孙节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<INode> children;

    /**
     * 是否有子孙节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

    @Override
    public List<INode> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return this.children;
    }

}
