package com.auiucloud.ums.service.impl;

import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.ums.domain.UserTask;
import com.auiucloud.ums.domain.UserTaskRecord;
import com.auiucloud.ums.enums.UserTaskEnums;
import com.auiucloud.ums.mapper.UserTaskRecordMapper;
import com.auiucloud.ums.service.IUserTaskRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dries
 * @description 针对表【ums_user_task_record(用户任务记录表)】的数据库操作Service实现
 * @createDate 2023-05-09 17:30:38
 */
@Service
public class UserTaskRecordServiceImpl extends ServiceImpl<UserTaskRecordMapper, UserTaskRecord>
        implements IUserTaskRecordService {

    @Override
    public List<UserTaskRecord> selectUserTaskRecordListByTask2UserId(UserTask task, Long userId) {

        List<UserTaskRecord> userTaskRecords = new ArrayList<>();
        UserTaskEnums.UserTaskTypeEnum enumByValue = IBaseEnum.getEnumByValue(task.getType(), UserTaskEnums.UserTaskTypeEnum.class);
        switch (enumByValue) {
            case NEW_USER_TASK -> // 新用户任务
                    userTaskRecords = this.selectUserTaskRecordListByTaskId2UserId(task.getId(), userId);
            case DAY_TASK -> // 日常任务
                    userTaskRecords = this.selectUserTaskRecordListByTaskId2UserId2Daily(task.getId(), userId, task.getTaskCycle());
            case ACTIVITY_TASK -> // 活动任务
                    userTaskRecords = selectUserTaskRecordListByTaskId2UserId(task.getId(), userId);
        }

        return userTaskRecords;
    }

    @Override
    public List<UserTaskRecord> selectUserTaskRecordListByTaskId2UserId(Long taskId, Long userId) {
        return Optional.ofNullable(this.list(new LambdaQueryWrapper<UserTaskRecord>()
                        .eq(UserTaskRecord::getUId, userId)
                        .eq(UserTaskRecord::getTaskId, taskId)
                        .orderByDesc(UserTaskRecord::getCreateBy)
                )).orElse(Collections.emptyList());
    }

    @Override
    public List<UserTaskRecord> selectUserTaskRecordByTaskId2UserId2Day(Long taskId, Long userId) {
        // 查询今日记录
        return Optional.ofNullable(this.list(new LambdaQueryWrapper<UserTaskRecord>()
                .eq(UserTaskRecord::getUId, userId)
                .eq(UserTaskRecord::getTaskId, taskId)
                .apply("to_days(create_time)=to_days(now())")
                .orderByDesc(UserTaskRecord::getCreateBy)
        )).orElse(Collections.emptyList());
    }

    @Override
    public List<UserTaskRecord> selectUserTaskRecordByTaskId2UserId2Week(Long taskId, Long userId) {
        // 查询每周记录
        return Optional.ofNullable(this.list(new LambdaQueryWrapper<UserTaskRecord>()
                .eq(UserTaskRecord::getUId, userId)
                .eq(UserTaskRecord::getTaskId, taskId)
                .apply("to_days(create_time)=to_days(now())")
                .orderByDesc(UserTaskRecord::getCreateBy)
        )).orElse(Collections.emptyList());
    }

    @Override
    public List<UserTaskRecord> selectUserTaskRecordByTaskId2UserId2Month(Long taskId, Long userId) {
        // 查询每月记录
        return Optional.ofNullable(this.list(new LambdaQueryWrapper<UserTaskRecord>()
                .eq(UserTaskRecord::getUId, userId)
                .eq(UserTaskRecord::getTaskId, taskId)
                .apply("YEARWEEK(date_format(create_time,'%Y-%m-%d'))=YEARWEEK(now())")
                .orderByDesc(UserTaskRecord::getCreateBy)
        )).orElse(Collections.emptyList());
    }

    @Override
    public List<UserTaskRecord> selectUserTaskRecordByTaskId2UserId2Year(Long taskId, Long userId) {
        // 查询每年记录
        return Optional.ofNullable(this.list(new LambdaQueryWrapper<UserTaskRecord>()
                .eq(UserTaskRecord::getUId, userId)
                .eq(UserTaskRecord::getTaskId, taskId)
                .apply("and YEAR(create_time)=YEAR(now())")
                .orderByDesc(UserTaskRecord::getCreateBy)
        )).orElse(Collections.emptyList());
    }

    @Override
    public List<UserTaskRecord> selectUserTaskRecordByTaskId2UserId(Long taskId, Long userId) {
        // 不限日期
        return Optional.ofNullable(this.list(new LambdaQueryWrapper<UserTaskRecord>()
                .eq(UserTaskRecord::getUId, userId)
                .eq(UserTaskRecord::getTaskId, taskId)
                .orderByDesc(UserTaskRecord::getCreateBy)
        )).orElse(Collections.emptyList());
    }

    @Override
    public List<UserTaskRecord> selectUserTaskRecordListByTaskId2UserId2Daily(Long taskId, Long userId, String taskCycle) {

        List<UserTaskRecord> userTaskRecords = new ArrayList<>();
        UserTaskEnums.UserTaskCycleEnum enumByValue = IBaseEnum.getEnumByValue(taskCycle, UserTaskEnums.UserTaskCycleEnum.class);
        switch (enumByValue) {
            case DAY -> {
                // 查询今日是否存在完成记录
                List<UserTaskRecord> userTaskRecordList = this.selectUserTaskRecordByTaskId2UserId2Day(taskId, userId);
                userTaskRecords.addAll(userTaskRecordList);
            }
            case WEEK -> {
                // 查询本周是否存在完成记录
                List<UserTaskRecord> userTaskRecordList = this.selectUserTaskRecordByTaskId2UserId2Week(taskId, userId);
                userTaskRecords.addAll(userTaskRecordList);
            }
            case MONTH -> {
                // 查询本月是否存在完成记录
                List<UserTaskRecord> userTaskRecordList = this.selectUserTaskRecordByTaskId2UserId2Month(taskId, userId);
                userTaskRecords.addAll(userTaskRecordList);
            }
            case YEAR -> {
                // 查询今年是否存在完成记录
                List<UserTaskRecord> userTaskRecordList = this.selectUserTaskRecordByTaskId2UserId2Year(taskId, userId);
                userTaskRecords.addAll(userTaskRecordList);
            }
            case FOREVER -> {
                List<UserTaskRecord> userTaskRecordList = this.selectUserTaskRecordByTaskId2UserId(taskId, userId);
                userTaskRecords.addAll(userTaskRecordList);
            }
        }

        return userTaskRecords;
    }
}




