package com.auiucloud.component.cms.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 我的收藏表
 *
 * @TableName ums_user_gallery_collection
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "ums_user_gallery_collection")
public class UserGalleryCollection implements Serializable {

    @Serial
    private static final long serialVersionUID = -978013133673460092L;
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
     * 被收藏的ID
     */
    private Long postId;

    /**
     * 收藏类型(0-作品 1-合集)
     */
    private Integer type;

    /**
     * 收藏状态(0-收藏 1-取消)
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
