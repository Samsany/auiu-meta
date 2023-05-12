package com.auiucloud.ums.service;

import com.auiucloud.ums.domain.UserFeedback;
import com.auiucloud.ums.vo.UserFeedbackVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dries
* @description 针对表【ums_user_feedback(用户意见反馈表)】的数据库操作Service
* @createDate 2023-05-11 23:23:40
*/
public interface IUserFeedbackService extends IService<UserFeedback> {

    boolean submitFeedback(UserFeedbackVO feedbackVO);
}
