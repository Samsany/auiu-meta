package com.auiucloud.admin.vo;

import com.auiucloud.core.common.tree.INode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由对象
 *
 * @author dries
 * @createDate 2022-06-08 16-48
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteVO implements INode {

    private static final long serialVersionUID = -8662478581249339874L;

    @ApiModelProperty(value = "主键")
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
     * 组件
     */
    private String component;

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
     * 永久显示根菜单
     */
    private Boolean alwaysShow;

    /**
     * 前端隐藏(0-否 1-是)
     */
    private Boolean hidden;

    private Meta meta;

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

    @Data
    public static class Meta {

        /**
         * 标题
         */
        private String title;
        /**
         * 图标
         */
        private String icon;
        /**
         * 开启缓存
         */
        private Boolean noCache;
        /**
         * 固定在tags-view中
         */
        private Boolean affix;
        /**
         * 隐藏面包屑
         */
        private Boolean breadcrumb;
        /**
         * 开启验证(0-否 1-是)
         */
        private Boolean requireAuth;
    }
}