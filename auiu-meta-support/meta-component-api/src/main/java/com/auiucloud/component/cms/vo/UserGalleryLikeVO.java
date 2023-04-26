package com.auiucloud.component.cms.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dries
 **/
@Data
public class UserGalleryLikeVO {

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