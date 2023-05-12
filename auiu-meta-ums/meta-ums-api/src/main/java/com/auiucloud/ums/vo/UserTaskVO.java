package com.auiucloud.ums.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTaskVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2862816717322728087L;

    private Long id;

    /**
     * 是否已完成
     */
    private boolean isFinished;

    /**
     * 任务标题
     */
    private String name;

    /**
     * 任务图标
     */
    private String icon;

    /**
     * 赠送积分
     */
    private Integer point;

    /**
     * 任务跳转路径
     */
    private String path;

    /**
     * 操作按钮文字
     */
    private String btnText;

    /**
     * 任务类型(0-新手任务；1-日常任务 2-活动任务 3-会员专享任务)
     */
    private Integer type;

    /**
     * 操作类型
     */
    private Integer actionType;

    /**
     * 指定用户等级
     */
    private Long userLevelId;

    /**
     * 指定用户类型（0-普通用户 1-会员用户）
     */
    private Integer userType;

    /**
     * 任务状态(0-启用 1-关闭)
     */
    private Integer status;

    /**
     * 任务次数
     */
    private Integer num;

    /**
     * 任务需完成次数
     */
    private Integer finishNum;

    /**
     * 任务完成次数
     */
    private Integer successNum;

    /**
     * 任务标识
     */
    private String taskTag;

    /**
     * 任务周期(年-y 月-m 周-w 日-d)
     */
    private String taskCycle;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 租户ID
     */
    private Long tenantId;

}
