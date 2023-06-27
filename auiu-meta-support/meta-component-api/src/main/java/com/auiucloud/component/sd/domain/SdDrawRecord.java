package com.auiucloud.component.sd.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.Date;

/**
 * SD绘画记录表
 *
 * @TableName sd_draw_record
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sd_draw_record")
public class SdDrawRecord extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -3638351103136569447L;

    /**
     * 绘画作品
     */
    private String drawPic;

    /**
     * 用户ID
     */
    private Long userId;

    // /**
    //  * 任务ID
    //  */
    // private String taskId;

    /**
     * 进度条
     */
    private Integer progress;

    /**
     * 等待队列
     */
    private Integer waitQueue;

    /**
     * 绘画类型
     */
    private Integer type;

    /**
     * 绘画配置
     */
    private String config;

    /**
     * 状态(-1-作品违规 0-排队等待中 1-正在生成 2-已完成 3-生成失败)
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
