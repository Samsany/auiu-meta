package com.auiucloud.core.database.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 搜索封装类
 *
 * @author dries
 * @date 2021/12/21
 */
@Data
@ApiModel(description = "搜索条件")
public class Search implements Serializable {

    private static final long serialVersionUID = 4822862487453945228L;

    /**
     * 关键词
     */
    @ApiModelProperty(value = "关键词")
    private String keyword;

    /**
     * 开始日期
     */
    @ApiModelProperty(hidden = true)
    private String startDate;

    /**
     * 结束日期
     */
    @ApiModelProperty(hidden = true)
    private String endDate;

    /**
     * 排序属性
     */
    @ApiModelProperty(hidden = true)
    private String prop;

    /**
     * 排序方式：asc,desc
     */
    @ApiModelProperty(hidden = true)
    private String order;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Integer pageNum = 1;

    /**
     * 每页的数量
     */
    @ApiModelProperty(value = "每页的数量")
    private Integer pageSize = 10;

}
