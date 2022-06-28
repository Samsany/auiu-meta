package com.auiucloud.uaa.controller;

import com.auiucloud.admin.dto.SysUserInfo;
import com.auiucloud.admin.feign.ISysUserProvider;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.model.LoginUser;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.redis.core.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dries
 * @createDate 2022-5-31 16:16
 */
@RestController
@Api(tags = "认证管理")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final RedisService redisService;

    private final ISysUserProvider sysUserProvider;

    // private final ISysRolePermissionProvider sysRolePermissionProvider;

    @GetMapping("/test")
    public ApiResult<?> testAuth() {
        return sysUserProvider.getUserByUsername("admin");
    }

    @Log(value = "用户信息", exception = "用户信息请求异常")
    @ApiOperation(value = "用户信息", notes = "用户信息")
    @GetMapping("/user/info")
    public ApiResult<?> getUser() {

        // 获取当前登录用户信息
        LoginUser loginUser = SecurityUtil.getUser();

        SysUserInfo userInfo = sysUserProvider.getUserByUsername(loginUser.getAccount()).getData();
        Map<String, Object> data = new HashMap<>(7);
        data.put("username", loginUser.getAccount());
        data.put("realName", userInfo.getSysUser().getRealName());
        data.put("nickname", userInfo.getSysUser().getNickname());
        data.put("avatar", userInfo.getSysUser().getAvatar());
        data.put("departId", userInfo.getSysUser().getDeptId());
        data.put("tenantId", userInfo.getTenantId());
        data.put("roles", userInfo.getRoles());
        data.put("permissions", userInfo.getPermissions());
        return ApiResult.data(data);
    }

}
