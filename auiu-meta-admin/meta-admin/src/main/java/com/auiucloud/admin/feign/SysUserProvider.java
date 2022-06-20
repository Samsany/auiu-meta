package com.auiucloud.admin.feign;

import cn.hutool.core.util.ObjectUtil;
import com.auiucloud.admin.domain.SysUser;
import com.auiucloud.admin.dto.SysUserInfo;
import com.auiucloud.admin.service.ISysUserRoleService;
import com.auiucloud.admin.service.ISysUserService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author dries
 * @date 2022/4/9
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "用户远程调用")
public class SysUserProvider implements ISysUserProvider {

    private final ISysUserService sysUserService;

    // private final ISysRolePermissionProvider sysRolePermissionProvider;

    private final ISysUserRoleService sysUserRoleService;

    @Override
    @GetMapping(ProviderConstant.PROVIDER_USER_USERNAME)
    @Log(value = "用户名查询用户", exception = "用户名查询用户请求失败")
    @ApiOperation(value = "用户用户名查询", notes = "用户用户名查询")
    public ApiResult<SysUserInfo> getUserByUsername(String username) {
        SysUser sysUser = sysUserService.getUserByUsername(username);
        SysUserInfo userInfo = this.getSysUserInfo(sysUser);
        return ApiResult.data(userInfo);
    }

    /**
     * 用户信息组装
     *
     * @param sysUser 用户信息
     * @return SysUserInfo
     */
    private SysUserInfo getSysUserInfo(SysUser sysUser) {
        if (ObjectUtil.isNull(sysUser)) {
            return null;
        }

        // 用户信息组装
        // sysRolePermissionProvider.getRolesByUserId();
        List<String> roles = sysUserRoleService.getRoleCodeListByUserId(sysUser.getId());
        SysUserInfo userInfo = SysUserInfo.builder()
                .sysUser(sysUser)
                .username(sysUser.getAccount())
                .roles(roles)
                .permissions(Collections.emptyList())
                .tenantId("")
                .build();
        log.debug("feign调用：userInfo:{}", userInfo);
        return userInfo;
    }

}
