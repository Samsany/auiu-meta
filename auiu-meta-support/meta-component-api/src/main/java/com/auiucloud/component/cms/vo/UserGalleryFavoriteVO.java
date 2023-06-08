package com.auiucloud.component.cms.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author dries
 **/
@Data
public class UserGalleryFavoriteVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 392303865286561760L;
    /**
     * 主键标识
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 类型(0-作品 1-合集)
     */
    private Integer type;

    /**
     * 状态(0-点赞/收藏 1-取消)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
