package com.auiucloud.ums.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 用户标签分类表
 * @TableName ums_user_tag_group
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName(value ="ums_user_tag_group")
@EqualsAndHashCode(callSuper = true)
public class UserTagGroup extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 8962912560095987601L;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

}
