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
 * 作品合集表
 * @TableName cms_gallery_collection
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value ="cms_gallery_collection")
public class GalleryCollection extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -2751442471654858353L;

    /**
     * 创建人ID
     */
    private Long uId;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 是否置顶(0-否 1-是)
     */
    private Integer isTop;

    /**
     * 状态(0-仅自己可见 1-广场可见)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 租户ID
     */
    private Long tenantId;
}
