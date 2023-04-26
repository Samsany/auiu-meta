package com.auiucloud.component.cms.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 我的点赞表
 * @TableName ums_user_gallery_like
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="ums_user_gallery_like")
public class UserGalleryLike implements Serializable {

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
     * 被点赞的ID
     */
    private Long postId;

    /**
     * 收藏类型(0-作品 1-合集)
     */
    private Integer type;

    /**
     * 点赞状态(0-收藏 1-取消)
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
