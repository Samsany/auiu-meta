package com.auiucloud.admin.feign;

import com.auiucloud.admin.dto.SysUserInfo;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.feign.constant.FeignConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户远程调用接口
 *
 * @author dries
 * @date 2022/2/10
 */
@FeignClient(value = FeignConstant.AUIU_MATE_CLOUD_ADMIN)
public interface ISysUserProvider {

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return ApiResult
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_USERNAME)
    ApiResult<SysUserInfo> getUserByUsername(@RequestParam String username);

}
