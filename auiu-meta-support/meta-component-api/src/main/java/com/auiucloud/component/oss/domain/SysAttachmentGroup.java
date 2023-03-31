package com.auiucloud.component.oss.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 附件分组表
 * @TableName sys_attachment_group
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value ="sys_attachment_group")
public class SysAttachmentGroup extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2775938737258288869L;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 路径
     */
    private String bizPath;

    /**
     * 分组图标
     */
    private String icon;

    /**
     * 分组类型(0-图片 1-视频)
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     *
     */
    private Integer tenantId;

}
