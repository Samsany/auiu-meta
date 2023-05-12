package com.auiucloud.ums.service;

import com.auiucloud.core.database.model.Search;
import com.auiucloud.ums.domain.UserTask;
import com.auiucloud.ums.dto.UserTaskCompleteDTO;
import com.auiucloud.ums.vo.UserTaskVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【ums_user_task(用户任务表)】的数据库操作Service
* @createDate 2023-05-09 17:30:38
*/
public interface IUserTaskService extends IService<UserTask> {

    List<UserTask> slelectUserTaskList(Search search, UserTask userTask);

    List<UserTaskVO> listUserTask(UserTask userTask);

    UserTaskVO getUserTaskById(Long taskId);

    boolean completeUserTask(UserTaskCompleteDTO taskDTO);

    boolean completeUserTask(Long taskId);
}
