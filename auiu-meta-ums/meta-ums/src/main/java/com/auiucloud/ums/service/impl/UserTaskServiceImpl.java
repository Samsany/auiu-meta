package com.auiucloud.ums.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.ums.domain.Member;
import com.auiucloud.ums.domain.UserIntegralRecord;
import com.auiucloud.ums.domain.UserTask;
import com.auiucloud.ums.domain.UserTaskRecord;
import com.auiucloud.ums.dto.UserTaskAwardDTO;
import com.auiucloud.ums.dto.UserTaskCompleteDTO;
import com.auiucloud.ums.enums.UserPointEnums;
import com.auiucloud.ums.enums.UserTaskEnums;
import com.auiucloud.ums.mapper.UserTaskMapper;
import com.auiucloud.ums.service.IMemberService;
import com.auiucloud.ums.service.IUserIntegralRecordService;
import com.auiucloud.ums.service.IUserTaskRecordService;
import com.auiucloud.ums.service.IUserTaskService;
import com.auiucloud.ums.vo.UserInfoVO;
import com.auiucloud.ums.vo.UserTaskVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dries
 * @description 针对表【ums_user_task(用户任务表)】的数据库操作Service实现
 * @createDate 2023-05-09 17:30:38
 */
@Service
@RequiredArgsConstructor
public class UserTaskServiceImpl extends ServiceImpl<UserTaskMapper, UserTask>
        implements IUserTaskService {

    private final IMemberService memberService;
    private final IUserTaskRecordService userTaskRecordService;
    private final IUserIntegralRecordService userIntegralRecordService;

    @Override
    public List<UserTaskVO> listUserTask(UserTask userTask) {
        Long userId = SecurityUtil.getUserId();

        // 获取任务列表
        List<UserTask> userTasks = Optional.ofNullable(this.list(Wrappers.<UserTask>lambdaQuery()
                        .eq(UserTask::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .eq(StrUtil.isNotBlank(userTask.getTaskTag()), UserTask::getTaskTag, userTask.getTaskTag())
                ))
                .orElse(Collections.emptyList());
        return userTasks.parallelStream()
                .filter(it -> checkUserTaskPerm(userId, it.getUserType()))
                .map(it -> covertUserTask2UserTaskVO(userId, it)).toList();
    }

    @Override
    public UserTaskVO getUserTaskById(Long taskId) {
        Long userId = SecurityUtil.getUserId();
        UserTask userTask = this.getById(taskId);
        if (ObjectUtil.isNotNull(userTask)) {
            return covertUserTask2UserTaskVO(userId, userTask);
        }
        return null;
    }

    @NotNull
    private UserTaskVO covertUserTask2UserTaskVO(Long userId, UserTask userTask) {
        UserTaskVO userTaskVO = new UserTaskVO();
        BeanUtils.copyProperties(userTask, userTaskVO);

        List<UserTaskRecord> userTaskRecords = userTaskRecordService.selectUserTaskRecordListByTask2UserId(userTask, userId);
        long count = userTaskRecords.parallelStream()
                .filter(it -> it.getTaskProgress().equals(userTask.getFinishNum()))
                .count();
        userTaskVO.setSuccessNum(Math.toIntExact(count));
        // 判断当前任务完成状态
        if (userTask.getTaskCycle().equals(UserTaskEnums.UserTaskCycleEnum.FOREVER.getValue())) {
            userTaskVO.setFinished(false);
        } else {
            userTaskVO.setFinished(userTaskRecords.size() == userTask.getNum());
        }
        return userTaskVO;
    }

    @Override
    public List<UserTask> slelectUserTaskList(Search search, UserTask userTask) {
        return list(new LambdaQueryWrapper<UserTask>().eq(UserTask::getStatus, CommonConstant.STATUS_NORMAL_VALUE));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean completeUserTask(Long taskId) {
        return this.completeUserTask(UserTaskCompleteDTO.builder()
                .taskId(taskId)
                .uId(SecurityUtil.getUserId())
                .build());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean completeUserTask(UserTaskCompleteDTO userTaskVO) {
        if (ObjectUtil.isNotNull(userTaskVO.getTaskId())) {
            Long userId = userTaskVO.getUId();
            UserTaskVO userTask = this.getUserTaskById(userTaskVO.getTaskId());

            if (ObjectUtil.isNotNull(userTask)) {
                if (!checkUserTaskPerm(userId, userTask.getUserType())) {
                    throw new ApiException(ResultCode.USER_ERROR_A0300);
                }
                if (userTask.isFinished()) {
                    throw new ApiException("任务已完成，不可重复");
                }

                Integer taskType = userTask.getType();
                List<UserTaskRecord> userTaskRecords;
                UserTaskEnums.UserTaskTypeEnum enumByValue = IBaseEnum.getEnumByValue(taskType, UserTaskEnums.UserTaskTypeEnum.class);
                switch (enumByValue) {
                    case NEW_USER_TASK, ACTIVITY_TASK -> {// 新用户任务 活动任务
                        userTaskRecords = userTaskRecordService.selectUserTaskRecordListByTaskId2UserId(userTask.getId(), userId);
                        addUserTaskRecord(userId, userTask, userTaskRecords);
                    }
                    case DAY_TASK -> {// 日常任务
                        userTaskRecords = userTaskRecordService.selectUserTaskRecordListByTaskId2UserId2Daily(userTask.getId(), userId, userTask.getTaskCycle());
                        addUserTaskRecord(userId, userTask, userTaskRecords);
                    }
                }
            } else {
                throw new ApiException("任务不存在或已结束");
            }
        }
        //        else if (StrUtil.isNotBlank(userTaskVO.getTaskTag())) {
        //            List<UserTaskVO> userTasks = this.listUserTask(UserTask.builder()
        //                    .taskTag(userTaskVO.getTaskTag())
        //                    .build());
        //        }

        return true;
    }

    private void addUserTaskRecord(Long userId, UserTaskVO userTask, List<UserTaskRecord> userTaskRecords) {
        if (CollUtil.isEmpty(userTaskRecords)) {
            // 判断任务次数是否达成 -> 发放奖励
            issueTaskReward(userId, userTask);
        } else {
            userTaskRecords
                    .stream()
                    .findFirst()
                    .ifPresent(userTaskRecord -> {
                        // 保存/更新记录 --> 发放奖励
                        if (userTaskRecord.getTaskProgress() < userTask.getFinishNum()) {
                            Integer finishNum = userTaskRecord.getTaskProgress() + 1;
                            userTaskRecord.setTaskProgress(finishNum);
                            userTaskRecordService.saveOrUpdate(userTaskRecord);
                            if (userTask.getFinishNum().equals(finishNum)) {
                                // 判断任务次数是否达成 -> 发放奖励
                                UserTaskEnums.UserTaskTagEnum enumByValue = IBaseEnum.getEnumByValue(userTask.getTaskTag(), UserTaskEnums.UserTaskTagEnum.class);
                                distributionReward(UserTaskAwardDTO.builder()
                                        .userId(userId)
                                        .point(userTask.getPoint())
                                        .tagEnum(enumByValue)
                                        .build());
                            }
                        } else {
                            // 判断任务次数是否达成 -> 发放奖励
                            issueTaskReward(userId, userTask);
                        }
                    });
        }
    }

    // 发放奖励并保存领取记录
    private void issueTaskReward(Long userId, UserTaskVO userTask) {
        if (userTask.getFinishNum() == 1) {
            // 判断任务次数是否达成 -> 发放奖励
            UserTaskEnums.UserTaskTagEnum enumByValue = IBaseEnum.getEnumByValue(userTask.getTaskTag(), UserTaskEnums.UserTaskTagEnum.class);
            distributionReward(UserTaskAwardDTO.builder()
                    .userId(userId)
                    .point(userTask.getPoint())
                    .tagEnum(enumByValue)
                    .build());
        }
        UserTaskRecord memberTaskLog = UserTaskRecord.builder()
                .uId(userId)
                .taskId(userTask.getId())
                .taskProgress(1)
                .build();
        userTaskRecordService.save(memberTaskLog);
    }

    // 发放奖励
    private void distributionReward(UserTaskAwardDTO taskAwardDTO) {
        Long userId = taskAwardDTO.getUserId();
        // 发放积分
        Integer point = taskAwardDTO.getPoint();
        if (point > 0) {
            // 更新用户积分
            Member member = memberService.getById(userId);
            Integer integral = member.getIntegral();
            memberService.increaseUserPoint(userId, point);

            // 保存用户积分记录
            UserIntegralRecord build = UserIntegralRecord.builder()
                    .uId(userId)
                    .title(taskAwardDTO.getTagEnum().getLabel())
                    .integral(point)
                    .balance(point + integral)
                    .status(UserPointEnums.StatusEnum.SUCCESS.getValue())
                    .type(UserPointEnums.ChangeTypeEnum.INCREASE.getValue())
                    .frozenTime(0)
                    .thawTime(LocalDateTime.now())
                    .build();
            userIntegralRecordService.save(build);
        }
    }


    /**
     * @param userId   用户ID
     * @param userType 任务指定的用户类型
     * @return boolean
     */
    private boolean checkUserTaskPerm(Long userId, Integer userType) {
        UserInfoVO userInfoVO = memberService.getUserInfoVOById(userId);

        UserTaskEnums.UserTaskUserTypeEnum enumByValue = IBaseEnum.getEnumByValue(userType, UserTaskEnums.UserTaskUserTypeEnum.class);
        switch (enumByValue) {
            case ALL -> {
                return true;
            }
            case ORDINARY_USER -> {
                return !userInfoVO.getIsPaidMember();
            }
            case MEMBER_USER -> {
                return userInfoVO.getIsPaidMember();
            }
            default -> {
                return false;
            }
        }
    }
}




