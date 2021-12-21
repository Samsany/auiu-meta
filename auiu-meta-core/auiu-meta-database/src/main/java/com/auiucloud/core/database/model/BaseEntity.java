package com.auiucloud.core.database.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类
 *
 * @author dries
 * @date 2021/12/21
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 595630788503119633L;

    @ApiModelProperty(value = "主键id")
    @TableId
    private Long id;

    /**
     * 创建人
     */
    @JsonIgnore
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
