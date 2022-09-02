package com.auiucloud.gen.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dries
 * @createDate 2022-09-02 18-03
 */
@Data
@ApiModel(value = "代码生成表字段传输对象")
public class GenTableColumnDTO implements Serializable {
    private static final long serialVersionUID = 474385254629577199L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private Long id;

    /**
     * 归属表编号
     */
    @ApiModelProperty(value = "归属表编号")
    private Long tableId;

    /**
     * 列名称
     */
    @ApiModelProperty(value = "列名称")
    private String columnName;

    /**
     * 列描述
     */
    @ApiModelProperty(value = "列描述")
    private String columnComment;

    /**
     * 列类型
     */
    @ApiModelProperty(value = "列类型")
    private String columnType;

    /**
     * JAVA类型
     */
    @ApiModelProperty(value = "JAVA类型")
    private String javaType;

    /**
     * JAVA字段名
     */
    @ApiModelProperty(value = "JAVA字段名")
    private String javaField;

    /**
     * 是否主键（1是）
     */
    @ApiModelProperty(value = "是否主键（1是）")
    private Integer isPk;

    /**
     * 是否自增（1是）
     */
    @ApiModelProperty(value = "是否自增（1是）")
    private Integer isIncrement;

    /**
     * 是否必填（1是）
     */
    @ApiModelProperty(value = "是否必填（1是）")
    private Integer isRequired;

    /**
     * 是否为插入字段（1是）
     */
    @ApiModelProperty(value = "是否为插入字段（1是）")
    private Integer isInsert;

    /**
     * 是否编辑字段（1是）
     */
    @ApiModelProperty(value = "是否编辑字段（1是）")
    private Integer isEdit;

    /**
     * 是否列表字段（1是）
     */
    @ApiModelProperty(value = "是否列表字段（1是）")
    private Integer isList;

    /**
     * 是否查询字段（1是）
     */
    @ApiModelProperty(value = "是否查询字段（1是）")
    private Integer isQuery;

    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    @ApiModelProperty(value = "查询方式（等于、不等于、大于、小于、范围）")
    private String queryType;

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    @ApiModelProperty(value = "显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）")
    private String htmlType;

    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型")
    private String dictType;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

}
