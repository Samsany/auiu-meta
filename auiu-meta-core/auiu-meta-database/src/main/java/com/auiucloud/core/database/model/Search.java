package com.auiucloud.core.database.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 搜索封装类
 *
 * @author dries
 * @date 2021/12/21
 */
@Data
@Tag(name = "Search", description = "搜索条件")
public class Search implements Serializable {

    @Serial
    private static final long serialVersionUID = 4822862487453945228L;

    /**
     * 关键词
     */
    @Schema(description = "关键词", hidden = true)
    private String keyword;

    /**
     * 状态
     */
    @Schema(description = "状态", hidden = true)
    private Integer status;

    /**
     * 开始日期
     */
    @Schema(description = "开始日期", hidden = true)
    private String startDate;

    /**
     * 结束日期
     */
    @Schema(description = "结束日期", hidden = true)
    private String endDate;

    /**
     * 排序属性
     */
    @Schema(description = "排序属性", hidden = true)
    private String prop;

    /**
     * 排序方式：asc,desc
     */
    @Schema(description = "排序方式", hidden = true)
    private String order;

    /**
     * 当前页
     */
    @Schema(description = "当前页", hidden = true)
    private Integer pageNum = 1;

    /**
     * 每页显示数据
     */
    @Schema(description = "每页显示数据", hidden = true)
    private Integer pageSize = 10;

    /**
     * 查询模式 page、list、cascade、tree
     * 默认：page
     */
    @Schema(description = "查询模式", hidden = true)
    private String queryMode;

}
