package com.auiucloud.admin.feign;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.feign.constant.FeignConstant;
import com.auiucloud.core.feign.fallback.MetaFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户角色远程调用接口
 *
 * @author dries
 * @createDate 2022-05-31 16-43
 */
@FeignClient(value = FeignConstant.AUIU_META_CLOUD_ADMIN, fallbackFactory = MetaFallbackFactory.class)
public interface ISysUserRoleProvider {

    /**
     * 根据用户ID查询角色标识列表
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_ROLE_LIST)
    ApiResult<List<String>> getRoleCodeListByUserId(@RequestParam Long userId);

}
