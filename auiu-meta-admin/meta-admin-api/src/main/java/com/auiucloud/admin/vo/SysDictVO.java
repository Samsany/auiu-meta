package com.auiucloud.admin.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class SysDictVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -596133727530673562L;
    /**
     * 字典ID
     */
    private Long id;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态(0-正常 1-停用)
     */
    private Integer status;

    private List<SysDictDataVO> dictDataList;

}
