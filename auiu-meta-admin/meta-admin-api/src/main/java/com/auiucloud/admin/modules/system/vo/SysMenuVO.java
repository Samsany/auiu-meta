package com.auiucloud.admin.modules.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author dries
 * @createDate 2022-08-08 00-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "系统菜单VO", description = "系统菜单")
public class SysMenuVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2559369572203501365L;

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

}
