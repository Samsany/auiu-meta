package com.auiucloud.admin.feign;

import com.auiucloud.admin.service.ISysRolePermissionService;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dries
 * @createDate 2022-06-27 16-46
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "角色权限远程调用")
public class SysRolePermissionProvider implements ISysRolePermissionProvider {

    private final ISysRolePermissionService sysRolePermissionService;

    @Override
    @PostMapping(ProviderConstant.PROVIDER_ROLE_PERMISSION_LIST)
    @Log(value = "获取权限列表", exception = "获取权限列表请求失败")
    @Operation(summary = "获取权限列表", description = "根据角色列表获取权限列表")
    // 配置一级缓存
    // @Cached(name = "getPermission-", key = "#roleId", expire = 120)
    public List<String> getPermissionListByRoles(List<String> roles) {
        log.info("feign调用getPermission，roles：{}", roles);
        List<String> permissions = sysRolePermissionService.getPermissionListByRoles(roles);
        log.info("permissions：{}", permissions);
        return permissions;
    }

}
