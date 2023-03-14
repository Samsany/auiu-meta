package com.auiucloud.admin.vo;

import com.auiucloud.core.common.tree.INode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
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
@Schema(name = "系统菜单树VO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenuTreeVO implements INode {
    @Serial
    private static final long serialVersionUID = -8662478581249339874L;

    @Schema(description = "序号")
    private Long id;
    @Schema(description = "上级ID")
    private Long parentId;
    @Schema(description = "组件名称")
    private String name;
    @Schema(description = "菜单名称")
    private String title;
    @Schema(description = "菜单权限")
    private String permission;
    @Schema(description = "组件路径")
    private String component;
    @Schema(description = "菜单图标")
    private String icon;
    @Schema(description = "路由路径")
    private String path;
    @Schema(description = "路由参数")
    private String queryParams;
    @Schema(description = "重定向地址，设置为noRedirect时，面包屑不可点击")
    private String redirect;
    @Schema(description = "打开方式 0-无 1-组件 2-内链 3-外链")
    private Integer openType;
    @Schema(description = "内外链地址")
    private String target;
    @Schema(description = "菜单类型(0-目录 1-菜单 2-外链)")
    private Integer type;
    @Schema(description = "菜单状态")
    private Integer status;
    @Schema(description = "永久显示根菜单")
    private Integer alwaysShow;
    @Schema(description = "固定在tags-view中")
    private Integer affix;
    @Schema(description = "前端隐藏")
    private Integer hidden;
    @Schema(description = "隐藏面包屑")
    private Integer hideHeader;
    @Schema(description = "开启缓存")
    private Integer keepalive;
    @Schema(description = "开启验证")
    private Integer requireAuth;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "创建人")
    private String createBy;
    @Schema(description = "更新人")
    private String updateBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "子孙节点")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<INode> children;

    @Schema(description = "是否有子孙节点")
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
