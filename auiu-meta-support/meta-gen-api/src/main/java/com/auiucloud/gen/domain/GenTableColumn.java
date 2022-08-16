package com.auiucloud.gen.domain;

import com.auiucloud.core.common.utils.StringUtils;
import com.auiucloud.gen.props.GenConstants;
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

    public static boolean isSuperColumn(String javaField) {
        return StringUtils.equalsAnyIgnoreCase(javaField,
                // BaseEntity
                "createBy", "createTime", "updateBy", "updateTime", "remark",
                // TreeEntity
                "parentName", "parentId", "sort", "ancestors");
    }

    public static boolean isUsableColumn(String javaField) {
        // isSuperColumn()中的名单用于避免生成多余Domain属性，若某些属性在生成页面时需要用到不能忽略，则放在此处白名单
        return StringUtils.equalsAnyIgnoreCase(javaField, "parentId", "sort", "remark");
    }

    public boolean isPk() {
        return isChecked(this.isPk);
    }

    public boolean isIncrement() {
        return isChecked(this.isIncrement);
    }

    public boolean isRequired() {
        return isChecked(this.isRequired);
    }

    public boolean isInsert() {
        return isChecked(this.isInsert);
    }

    public boolean isEdit() {
        return isChecked(this.isEdit);
    }

    public boolean isList() {
        return isChecked(this.isList);
    }

    public boolean isQuery() {
        return isChecked(this.isQuery);
    }

    private boolean isChecked(Integer value) {
        return value != null && value.equals(GenConstants.REQUIRE);
    }

    public boolean isSuperColumn() {
        return isSuperColumn(this.javaField);
    }

    public boolean isUsableColumn() {
        return isUsableColumn(javaField);
    }

    public String readConverterExp() {
        String remarks = StringUtils.substringBetween(this.columnComment, "（", "）");
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(remarks)) {
            for (String value : remarks.split(" ")) {
                if (StringUtils.isNotEmpty(value)) {
                    Object startStr = value.subSequence(0, 1);
                    String endStr = value.substring(1);
                    sb.append("").append(startStr).append("=").append(endStr).append(",");
                }
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        } else {
            return this.columnComment;
        }
    }
}
