package com.auiucloud.component.sd.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 图片比例表
 * @TableName sd_pic_ratio
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value ="sd_pic_ratio")
public class SdPicRatio extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2529504527294591506L;

    /**
     * 标题
     */
    private String title;

    /**
     * 比例
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
     * 消耗积分
     */
    private Integer consumeIntegral;

    /**
     * 状态(0-正常 1-隐藏)
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
