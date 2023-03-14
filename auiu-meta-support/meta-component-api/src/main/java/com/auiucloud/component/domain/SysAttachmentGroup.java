package com.auiucloud.component.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 附件分组表
 * @TableName sys_attachment_group
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value ="sys_attachment_group")
public class SysAttachmentGroup extends BaseEntity {

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     *
     */
    private Integer tenantId;

}
