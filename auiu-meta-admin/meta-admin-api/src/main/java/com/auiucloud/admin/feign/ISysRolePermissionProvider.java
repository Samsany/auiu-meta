package com.auiucloud.admin.feign;

import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.feign.constant.FeignConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 角色权限远程调用接口
 *
 * @author dries
 * @date 2022/2/10
 */
@FeignClient(value = FeignConstant.META_CLOUD_ADMIN)
public interface ISysRolePermissionProvider {

    @PostMapping(ProviderConstant.PROVIDER_ROLE_PERMISSION_LIST)
    List<String> getPermissionListByRoles(@RequestBody List<String> roles);

}
