package com.auiucloud.admin.feign;

import com.auiucloud.admin.modules.system.vo.UserInfoVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.feign.constant.FeignConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户远程调用接口
 *
 * @author dries
 * @date 2022/2/10
 */
@Component
@FeignClient(value = FeignConstant.META_CLOUD_ADMIN)
public interface ISysUserProvider {

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return ApiResult
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_USERNAME)
    ApiResult<UserInfoVO> getUserByUsername(@RequestParam("username") String username);

    @GetMapping(ProviderConstant.PROVIDER_USER_OPENID)
    ApiResult<UserInfoVO> getSysUserByOpenId2Source(@RequestParam("openId") String openId, @RequestParam("source") String source);
}
