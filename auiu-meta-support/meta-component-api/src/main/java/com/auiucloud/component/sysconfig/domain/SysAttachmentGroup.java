package com.auiucloud.component.sysconfig.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 附件分组表
 *
 * @TableName sys_attachment_group
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_attachment_group")
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
