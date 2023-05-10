package com.auiucloud.admin.modules.system.vo;

import com.auiucloud.core.common.tree.INode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 路由对象
 *
 * @author dries
 * @createDate 2022-06-08 16-48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "系统路由VO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteVO implements INode {

    private static final long serialVersionUID = -8662478581249339874L;

    @Schema(description = "序号")
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
     * 菜单权限
     */
    private String permission;

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
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String redirect;

    /**
     * 菜单类型(0-目录 1-菜单 2-按钮)
     */
    private Integer type;

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
    public static class Meta implements Serializable {
        private static final long serialVersionUID = 6274249000010676281L;
        /**
         * 标题
         */
        private String title;
        /**
         * 图标
         */
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String icon;
        /**
         * 永久显示根菜单
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean alwaysShow = false;

        /**
         * 前端隐藏(0-否 1-是)
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean hidden = true;
        /**
         * 开启缓存
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean noCache = true;
        /**
         * 内链地址
         */
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String frameSrc;
        /**
         * 固定在tags-view中
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean affix = true;
        /**
         * 隐藏面包屑
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean breadcrumb = true;
        /**
         * 携带参数
         */
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String queryParams;
        /**
         * 开启验证(0-否 1-是)
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean requireAuth = true;
        /**
         * 排序
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer sort;
    }
}
