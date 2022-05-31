package com.auiucloud.core.database.model;

import com.baomidou.mybatisplus.annotation.TableField;
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

    @ApiModelProperty(value = "主键ID")
    @TableId
    private Long id;

    /**
     * 创建人
     */
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

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 删除标识
     */
    @JsonIgnore
    @ApiModelProperty(value = "删除标识(0-存在 1-已删除)")
    @TableField(value = "is_deleted")
    private boolean deleted;
}
