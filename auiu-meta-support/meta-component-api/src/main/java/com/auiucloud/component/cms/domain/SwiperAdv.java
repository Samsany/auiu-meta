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
 * 轮播广告表
 * @TableName cms_swiper_adv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName(value ="cms_swiper_adv")
@EqualsAndHashCode(callSuper = true)
public class SwiperAdv extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -3132498387589385666L;

    /**
     * 名字
     */
    private String name;

    /**
     * 图片地址
     */
    private String pic;

    /**
     * 点击数
     */
    private Integer clickNum;

    /**
     * 展示类型(0-首页)
     */
    private Integer type;

    /**
     * 跳转类型(0-内链 1-外链 2-外部小程序)
     */
    private Integer linkType;

    /**
     * 跳转链接
     */
    private String link;

    /**
     * 状态(0-正常 1-隐藏)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;
}
