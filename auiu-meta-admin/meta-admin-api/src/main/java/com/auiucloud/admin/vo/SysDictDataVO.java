package com.auiucloud.admin.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SysDictDataVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5529800843779511337L;
    private Long id;
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

    /**
     * 字典排序
     */
    private Integer sort;

}
