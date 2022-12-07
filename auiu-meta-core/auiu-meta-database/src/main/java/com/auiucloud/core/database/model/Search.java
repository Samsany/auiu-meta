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
    @ApiModelProperty(value = "关键词", hidden = true)
    private String keyword;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", hidden = true)
    private Integer status;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期", hidden = true)
    private String startDate;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期", hidden = true)
    private String endDate;

    /**
     * 排序属性
     */
    @ApiModelProperty(value = "排序属性", hidden = true)
    private String prop;

    /**
     * 排序方式：asc,desc
     */
    @ApiModelProperty(value = "排序方式", hidden = true)
    private String order;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页", hidden = true)
    private Integer pageNum = 1;

    /**
     * 每页显示数据
     */
    @ApiModelProperty(value = "每页显示数据", hidden = true)
    private Integer pageSize = 10;

    /**
     * 查询模式 page、list、cascade、tree
     * 默认：page
     */
    @ApiModelProperty(value = "查询模式", hidden = true)
    private String queryMode;

}
