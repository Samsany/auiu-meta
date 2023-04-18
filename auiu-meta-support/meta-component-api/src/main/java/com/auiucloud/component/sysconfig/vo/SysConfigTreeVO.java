package com.auiucloud.component.sysconfig.vo;

import com.auiucloud.core.common.tree.INode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "系统配置树VO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysConfigTreeVO implements INode {

    @Serial
    private static final long serialVersionUID = -3588850135193779378L;

    private Long Id;

    /**
     * 父主键
     */
    private Long parentId;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数类型
     */
    private String configCode;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 参数类型(0-配置分类 1-配置值)
     */
    private Integer configType;

    /**
     * 是否内置(0-否 1-是)
     */
    private Integer builtIn;

    /**
     * 排序
     */
    private Integer sort;

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
