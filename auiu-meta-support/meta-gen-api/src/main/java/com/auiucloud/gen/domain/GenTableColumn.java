package com.auiucloud.gen.domain;

import com.auiucloud.gen.utils.GenUtils;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 代码生成业务表字段
 *
 * @TableName gen_table_column
 */
@Data
@TableName(value = "gen_table_column")
public class GenTableColumn implements Serializable {
    private static final long serialVersionUID = 474385254629577199L;

    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 归属表编号
     */
    private Long tableId;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列描述
     */
    private String columnComment;

    /**
     * 列类型
     */
    private String columnType;

    /**
     * JAVA类型
     */
    private String javaType;

    /**
     * JAVA字段名
     */
    private String javaField;

    /**
     * 是否主键（1是）
     */
    private Integer isPk;

    /**
     * 是否自增（1是）
     */
    private Integer isIncrement;

    /**
     * 是否必填（1是）
     */
    private Integer isRequired;

    /**
     * 是否为插入字段（1是）
     */
    private Integer isInsert;

    /**
     * 是否编辑字段（1是）
     */
    private Integer isEdit;

    /**
     * 是否列表字段（1是）
     */
    private Integer isList;

    /**
     * 是否查询字段（1是）
     */
    private Integer isQuery;

    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    private String queryType;

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    private String htmlType;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    public boolean isSuperColumn() {
        return GenUtils.isSuperColumn(this.javaField);
    }

    public boolean isUsableColumn() {
        return GenUtils.isUsableColumn(this.javaField);
    }

}
