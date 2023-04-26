package com.auiucloud.gen.dto;

import com.auiucloud.core.common.utils.StringUtils;
import com.auiucloud.gen.constant.GenConstants;
import com.auiucloud.gen.domain.GenTableColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 * @createDate 2022-08-16 11-27
 */
@Data
@Schema(name = "代码生成表传输对象")
public class GenTableDTO implements Serializable {
    private static final long serialVersionUID = -2499462234269077050L;

    /**
     * 编号
     */
    @Schema(description = "编号")
    private Long id;

    /**
     * 表名称
     */
    @NotBlank(message = "表名称不能为空")
    @Schema(description = "表名称")
    private String tableName;

    /**
     * 表描述
     */
    @NotBlank(message = "表描述不能为空")
    @Schema(description = "表描述")
    private String tableComment;

    /**
     * 关联子表的表名
     */
    @Schema(description = "关联子表的表名")
    private String subTableName;

    /**
     * 子表关联的外键名
     */
    @Schema(description = "子表关联的外键名")
    private String subTableFkName;

    /**
     * 实体类名称
     */
    @NotBlank(message = "实体类名称不能为空")
    @Schema(description = "实体类名称")
    private String className;

    /**
     * 使用的模板（crud单表操作 tree树表操作 sub主子表操作）
     */
    @Schema(description = "使用的模板（crud单表操作 tree树表操作 sub主子表操作）")
    private String tplCategory;

    /**
     * 生成包路径
     */
    @NotBlank(message = "生成包路径不能为空")
    @Schema(description = "包路径")
    private String packageName;

    /**
     * 生成模块名
     */
    @NotBlank(message = "生成模块名不能为空")
    @Schema(description = "模块名")
    private String moduleName;

    /**
     * 生成业务名
     */
    @NotBlank(message = "生成业务名不能为空")
    @Schema(description = "业务名")
    private String businessName;

    /**
     * 生成功能名
     */
    @NotBlank(message = "生成功能名不能为空")
    @Schema(description = "功能名")
    private String functionName;

    /**
     * 生成功能作者
     */
    @NotBlank(message = "作者不能为空")
    @Schema(description = "作者")
    private String functionAuthor;

    /**
     * 生成代码方式（0zip压缩包 1自定义路径）
     */
    @Schema(description = "生成代码方式（0zip压缩包 1自定义路径）")
    private String genType;

    /**
     * 生成路径（不填默认项目路径）
     */
    @Schema(description = "生成路径（不填默认项目路径）")
    private String genPath;

    /**
     * 其它生成选项
     */
    @Schema(description = "其它生成选项")
    private String options;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 主键信息
     */
    @Schema(description = "主键信息")
    private GenTableColumn pkColumn;

    /**
     * 子表信息
     */
    @Schema(description = "子表信息")
    private GenTableDTO subTable;

    /**
     * 表列信息
     */
    @Valid
    @Schema(description = "表列信息")
    private List<GenTableColumn> columns;

    /**
     * 树编码字段
     */
    @Schema(description = "树编码")
    private String treeCode;

    /**
     * 树父编码字段
     */
    @Schema(description = "树父编码")
    private String treeParentCode;

    /**
     * 树名称字段
     */
    @Schema(description = "树名称")
    private String treeName;

    /**
     * 上级菜单ID字段
     */
    @Schema(description = "上级菜单ID")
    private String parentMenuId;

    /**
     * 上级菜单名称字段
     */
    @Schema(description = "上级菜单名称")
    private String parentMenuName;

    /**
     * 继承公共父类(0-禁用,1-启用)
     */
    @Schema(description = "继承公共父类(0-禁用,1-启用)")
    private Integer superEntityClass = 1;

    /**
     * 启用swagger(0-禁用,1-启用)
     */
    @Schema(description = "启用swagger注解(0-禁用,1-启用)")
    private Integer enableSwagger = 1;

    public boolean isSuperColumn(String javaField) {
        return StringUtils.equalsAnyIgnoreCase(javaField, GenConstants.BASE_ENTITY);
    }

}
