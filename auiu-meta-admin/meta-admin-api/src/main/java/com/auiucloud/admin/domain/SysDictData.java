package com.auiucloud.admin.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 字典数据表
 *
 * @TableName sys_dict_data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_dict_data")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysDictData对象", description = "字典数据表")
public class SysDictData extends BaseEntity {
    private static final long serialVersionUID = -8663485080847494960L;

    /**
     * 字典排序
     */
    private Integer sort;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;

    /**
     * 表格回显样式
     */
    private String listClass;

    /**
     * 是否默认（1-是 0-否）
     */
    private Integer isDefault;

    /**
     * 状态(0-正常 1-停用)
     */
    private Integer status;

}
