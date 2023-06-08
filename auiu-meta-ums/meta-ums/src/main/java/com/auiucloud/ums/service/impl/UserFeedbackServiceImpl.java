package com.auiucloud.ums.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.common.utils.http.RequestHolder;
import com.auiucloud.core.douyin.config.AppletsConfiguration;
import com.auiucloud.core.douyin.service.DouyinAppletsService;
import com.auiucloud.ums.domain.UserFeedback;
import com.auiucloud.ums.mapper.UserFeedbackMapper;
import com.auiucloud.ums.service.IUserFeedbackService;
import com.auiucloud.ums.vo.UserFeedbackVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【ums_user_feedback(用户意见反馈表)】的数据库操作Service实现
 * @createDate 2023-05-11 23:23:40
 */
@Service
public class UserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedback>
        implements IUserFeedbackService {

    @Override
    public boolean submitFeedback(UserFeedbackVO feedbackVO) {
        // 校验联系方式（只能为手机号或邮箱）
        if (StrUtil.isNotBlank(feedbackVO.getContactInformation()) &&
                !(Validator.isEmail(feedbackVO.getContactInformation())
                        || Validator.isMobile(feedbackVO.getContactInformation()))) {
            throw new ApiException("请填写正确的联系方式");
        }
        // 文本校验
        try {
            String appId = RequestHolder.getHttpServletRequestHeader("appId");
            Integer loginType = SecurityUtil.getUser().getLoginType();
            if (loginType.equals(AuthenticationIdentityEnum.DOUYIN_APPLET.getValue())) {
                DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
                boolean result = douyinAppletService.checkText(feedbackVO.getContent());
                if (result) {
                    throw new ApiException(ResultCode.USER_ERROR_A0431);
                }
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }

        UserFeedback userFeedback = new UserFeedback();
        BeanUtils.copyProperties(feedbackVO, userFeedback);
        try {
            Long userId = SecurityUtil.getUserId();
            userFeedback.setUserId(userId);
            return this.save(userFeedback);
        } catch (Exception e) {
            throw new ApiException("请登录后提交反馈");
        }
    }
}




