package com.auiucloud.uaa.controller;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.admin.dto.SysUserInfo;
import com.auiucloud.admin.feign.ISysUserProvider;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.model.LoginUser;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.redis.core.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
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
@Tag(name = "认证管理")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final ConsumerTokenServices consumerTokenServices;

    private final RedisService redisService;

    private final ISysUserProvider sysUserProvider;

    // private final ISysRolePermissionProvider sysRolePermissionProvider;

    @GetMapping("/test")
    public ApiResult<?> testAuth() {
        return sysUserProvider.getUserByUsername("admin");
    }

    @Log(value = "用户信息", exception = "用户信息请求异常")
    @Operation(summary = "用户信息", description = "用户信息")
    @GetMapping("/user/info")
    public ApiResult<?> getUser() {

        // 获取当前登录用户信息
        LoginUser loginUser = SecurityUtil.getUser();

        // TODO 判断用户所属客户端
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
        // 存入redis,以用于查询权限使用
        redisService.set(Oauth2Constant.META_PERMISSION_PREFIX + loginUser.getAccount(), data);
        return ApiResult.data(data);
    }

    @Log(value = "退出登录", exception = "退出登录请求异常")
    @GetMapping("/logout")
    @Operation(summary = "退出登录", description = "退出登录")
    @Parameters({
            @Parameter(name = "Authorization", required = true, description = "授权类型", in = ParameterIn.HEADER)
    })
    public ApiResult<?> logout() {
        String token = SecurityUtil.getHeaderToken();
        if (StringUtil.isNotBlank(token)) {
            consumerTokenServices.revokeToken(token);
        }
        return ApiResult.success("操作成功");
    }

}
