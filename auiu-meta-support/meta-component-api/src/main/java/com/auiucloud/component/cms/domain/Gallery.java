package com.auiucloud.component.cms.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 作品表
 *
 * @TableName cms_gallery
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "cms_gallery")
public class Gallery extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 7849471655351236866L;
    /**
     * 创建人ID
     */
    private Long userId;

    /**
     * 标题
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
     * 图片比例
     */
    private String ratio;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;

    /**
     * 大小
     */
    private Long size;

    /**
     * 绘画配置
     */
    private String sdConfig;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 合集ID
     */
    private Long collectionId;

    /**
     * 是否发布(0-否 1-是)
     */
    private Integer isPublished;

    /**
     * 是否置顶(0-否 1-是)
     */
    private Integer isTop;

    /**
     * 消耗积分
     */
    private Integer consumeIntegral;

    /**
     * 下载积分(开通会员可设置)
     */
    private Integer downloadIntegral;

    /**
     * 下载次数
     */
    private Long downloadTimes;

    /**
     * 浏览次数
     */
    private Long views;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 审核状态(-1-生成失败 0-等待生成 1-进行中 2-已完成)
     */
    private Integer status;

    /**
     * 审核状态(0-待审核 1-审核通过 2-已驳回)
     */
    private Integer approvalStatus;

    /**
     * 作品类型(0-壁纸 2-AI创作)
     */
    private Integer type;

    /**
     * 发布时间
     */
    private LocalDateTime publishedTime;

    /**
     * 加入合集时间
     */
    private LocalDateTime joinCollectionTime;

    /**
     * 租户ID
     */
    private Long tenantId;

}
