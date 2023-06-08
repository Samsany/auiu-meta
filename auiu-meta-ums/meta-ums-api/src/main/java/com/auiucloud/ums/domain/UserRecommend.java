package com.auiucloud.ums.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 推荐用户表
 * @TableName ums_user_recommend
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="ums_user_recommend")
public class UserRecommend implements Serializable {

    @Serial
    private static final long serialVersionUID = -6677852134798853202L;

    /**
     * 序号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 推荐状态(0-推荐 1-取消推荐)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 截止时间
     */
    private LocalDateTime deadline;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
