package com.auiucloud.component.cms.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author dries
 **/
@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "作品审核VO")
public class GalleryReviewVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 9115395963856349727L;

    private Long id;

    /**
     * 作品ID
     */
    private Long galleryId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 申诉理由
     */
    private String reason;

    /**
     * 状态(0-待处理 1-审核通过 2-驳回)
     */
    private Integer status;

    /**
     * 创建人昵称
     */
    private String nickname;

    /**
     * 创建人头像
     */
    private String avatar;

    /**
     * 作品标题
     */
    private String title;

    /**
     * 原图
     */
    private String pic;

    /**
     * 缩略图
     */
    private String thumbUrl;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 标签
     */
    private String tag;

    /**
     * 合集ID
     */
    private Long collectionId;

    /**
     * 合集名称
     */
    private String collection;

    /**
     * 审核状态(0-待审核 1-审核通过 2-已驳回)
     */
    private Integer approvalStatus;

    /**
     * 作品类型(0-壁纸 1-文生图 2-图生图)
     */
    private Integer type;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
}
