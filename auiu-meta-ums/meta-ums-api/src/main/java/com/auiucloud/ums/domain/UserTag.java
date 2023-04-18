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
 * 用户标签表
 * @TableName ums_user_tag
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName(value ="ums_user_tag")
@EqualsAndHashCode(callSuper = true)
public class UserTag extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -8161784238217748476L;


    /**
     * 标签分组id
     */
    private Long groupId;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

}
