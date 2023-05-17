package com.auiucloud.ums.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 我的关注粉丝表
 *
 * @TableName ums_user_follower
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "ums_user_follower")
public class UserFollower implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long uId;

    /**
     * 粉丝ID
     */
    private Long followerId;

    /**
     * 关注状态(0-关注 1-取消)
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
