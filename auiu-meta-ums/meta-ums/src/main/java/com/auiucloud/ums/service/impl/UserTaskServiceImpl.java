package com.auiucloud.ums.service.impl;

import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.ums.domain.UserTask;
import com.auiucloud.ums.domain.UserTaskRecord;
import com.auiucloud.ums.enums.UserTaskEnums;
import com.auiucloud.ums.mapper.UserTaskMapper;
import com.auiucloud.ums.service.IMemberService;
import com.auiucloud.ums.service.IUserTaskRecordService;
import com.auiucloud.ums.service.IUserTaskService;
import com.auiucloud.ums.vo.UserTaskVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<UserTaskVO> listUserTask(UserTask userTask) {
        Long userId = SecurityUtil.getUserId();
        // 获取任务列表
        List<UserTask> userTasks = Optional.ofNullable(this.list(Wrappers.<UserTask>lambdaQuery()
                        .eq(UserTask::getStatus, CommonConstant.STATUS_NORMAL_VALUE)))
                .orElse(Collections.emptyList());
        return userTasks.parallelStream()
                .map(it -> {
                    UserTaskVO userTaskVO = new UserTaskVO();
                    BeanUtils.copyProperties(it, userTaskVO);

                    // 判断当前任务完成状态
                    if (it.getTaskCycle().equals(UserTaskEnums.UserTaskCycleEnum.FOREVER.getValue())) {
                        userTaskVO.setFinished(false);
                    } else {
                        List<UserTaskRecord> userTaskRecords = userTaskRecordService.selectUserTaskRecordListByTask2UserId(it, userId);
                        userTaskVO.setFinished(userTaskRecords.size() == it.getNum());
                    }
                    return userTaskVO;
                }).toList();
    }

    @Override
    public List<UserTask> slelectUserTaskList(Search search, UserTask userTask) {
        return list(new LambdaQueryWrapper<UserTask>().eq(UserTask::getStatus, 1));
    }

}




