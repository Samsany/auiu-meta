package com.auiucloud.auth.feign;

import com.auiucloud.auth.domain.SocialUser;
import com.auiucloud.auth.service.SocialUserService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "第三方用户远程调用")
public class SocialProvider implements ISocialProvider {

    private final SocialUserService socialUserService;


    /**
     * 获取第三方用户信息
     *
     * @param openId 用户名
     * @param source 第三方平台
     * @return ApiResult
     */
    @Override
    public ApiResult<SocialUser> getSocialUserByOpenId2Source(String openId, String source) {
        return null;
    }

    /**
     * 第三方用户注册
     *
     * @param socialUser 第三方用户注册
     * @return ApiResult
     */
    @Override
    @PostMapping(ProviderConstant.PROVIDER_USER_REGISTER_SOCIAL)
    @Log(value = "第三方用户注册", exception = "第三方用户注册异常")
    @Operation(summary = "第三方用户注册", description = "第三方用户注册")
    public ApiResult<SocialUser> registerUserBySocial(Long userId, SocialUser socialUser) {
        return ApiResult.condition(socialUserService.registerUserBySocial(userId, socialUser));
    }

}
