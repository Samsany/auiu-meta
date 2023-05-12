package com.auiucloud.ums.service;

import com.auiucloud.ums.domain.UserTask;
import com.auiucloud.ums.domain.UserTaskRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【ums_user_task_record(用户任务记录表)】的数据库操作Service
* @createDate 2023-05-09 17:30:38
*/
public interface IUserTaskRecordService extends IService<UserTaskRecord> {

    List<UserTaskRecord> selectUserTaskRecordListByTask2UserId(UserTask task, Long userId);

    List<UserTaskRecord> selectUserTaskRecordListByTaskId2UserId(Long taskId, Long userId);

    List<UserTaskRecord> selectUserTaskRecordByTaskId2UserId2Day(Long taskId, Long userId);

    List<UserTaskRecord> selectUserTaskRecordByTaskId2UserId2Week(Long taskId, Long userId);

    List<UserTaskRecord> selectUserTaskRecordByTaskId2UserId2Month(Long taskId, Long userId);

    List<UserTaskRecord> selectUserTaskRecordByTaskId2UserId2Year(Long taskId, Long userId);

    List<UserTaskRecord> selectUserTaskRecordByTaskId2UserId(Long taskId, Long userId);

    List<UserTaskRecord> selectUserTaskRecordListByTaskId2UserId2Daily(Long taskId, Long userId, String taskCycle);
}
