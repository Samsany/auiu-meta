package com.auiucloud.ums.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 用户分组表
 * @TableName ums_user_group
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName(value ="ums_user_group")
@EqualsAndHashCode(callSuper = true)
public class UserGroup extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 排序
     */
    private Integer sort;

}
