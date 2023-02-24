package com.auiucloud.admin.feign;

import com.auiucloud.admin.service.ISysUserRoleService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dries
 * @createDate 2022-05-31 16-52
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "用户角色远程调用")
public class SysUserRoleProvider implements ISysUserRoleProvider {

    private final ISysUserRoleService sysUserRoleService;

    @Override
    @GetMapping(ProviderConstant.PROVIDER_USER_ROLE_LIST)
    @Log(value = "用户ID查询用户角色编码", exception = "用户ID查询用户角色编码请求失败")
    @Operation(summary = "根据用户ID查询用户角色编码", description = "根据用户ID查询用户角色编码")
    public ApiResult<List<String>> getRoleCodeListByUserId(Long userId) {
        List<String> roleCodeList = sysUserRoleService.getRoleCodeListByUserId(userId);
        return ApiResult.data(roleCodeList);
    }

}
