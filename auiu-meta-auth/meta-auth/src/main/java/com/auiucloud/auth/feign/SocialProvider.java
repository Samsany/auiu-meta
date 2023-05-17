package com.auiucloud.auth.feign;

import com.auiucloud.auth.domain.SocialUser;
import com.auiucloud.auth.service.ISocialUserService;
import com.auiucloud.core.common.api.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "第三方用户远程调用")
public class SocialProvider implements ISocialProvider {

    private final ISocialUserService socialUserService;


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

}
