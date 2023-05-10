package com.auiucloud.ums.domain;

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
 * 用户任务记录表
 * @TableName ums_user_task_record
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName(value ="ums_user_task_record")
@EqualsAndHashCode(callSuper = true)
public class UserTaskRecord extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -3515147033979304731L;

    /**
     * 用户ID
     */
    private String uId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务进度
     */
    private Integer taskProgress;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户ID
     */
    private Long tenantId;

}
