package com.auiucloud.component.cms.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGalleryLike2FavoriteVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1445801555725762050L;

    /**
     * 主键标识
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long uId;

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
     * 封面图
     */
    private List<String> covers;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子点赞数
     */
    @Builder.Default
    private Integer likeNum = 0;

    @Builder.Default
    private Boolean isLike = true;

    /**
     * 帖子收藏数
     */
    @Builder.Default
    private Integer favoriteNum = 0;

    @Builder.Default
    private Boolean isFavorite = true;

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
