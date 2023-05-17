package com.auiucloud.ums.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 用户任务表
 *
 * @TableName ums_user_task
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName(value = "ums_user_task")
@EqualsAndHashCode(callSuper = true)
public class UserTask extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 6795961709949037786L;

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
     * 任务类型(0-新手任务；1-日常任务 2-活动任务)
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
     * 指定用户类型（-1-不限 0-普通用户 1-会员用户）
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
     * 任务达成次数
     */
    private Integer finishNum;

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
