package com.auiucloud.auth.feign;

import com.auiucloud.auth.domain.SocialUser;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.feign.constant.FeignConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 社会化用户远程调用接口
 *
 * @author dries
 **/
@FeignClient(value = FeignConstant.META_CLOUD_AUTH)
public interface ISocialProvider {

    /**
     * 获取第三方用户信息
     *
     * @param openId 用户名
     * @return ApiResult
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_USERNAME)
    ApiResult<SocialUser> getSocialUserByOpenId2Source(@RequestParam("openId") String openId, @RequestParam("source") String source);

}
