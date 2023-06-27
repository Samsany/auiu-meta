package com.auiucloud.component.cms.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 作品审核表
 * @TableName cms_gallery_review
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value ="cms_gallery_review")
public class GalleryReview extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -4881498271912250246L;

    /**
     * 作品ID
     */
    private Long galleryId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 审核理由
     */
    private String reason;

    /**
     * 状态(0-待处理 1-审核通过 2-驳回)
     */
    private Integer status;

    /**
     * 租户ID
     */
    private Long tenantId;

}
