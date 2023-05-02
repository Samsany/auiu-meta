package com.auiucloud.auth.controller;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

/**
 * @author dries
 * @createDate 2022-5-31 16:16
 */
@Slf4j
@RestController
@Tag(name = "认证管理")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final ConsumerTokenServices consumerTokenServices;

//    private final RedisService redisService;
//    private final ISysUserProvider sysUserProvider;
//
//    @Log(value = "用户信息", exception = "用户信息请求异常")
//    @Operation(summary = "用户信息", description = "用户信息")
//    @GetMapping("/user/info")
//    public ApiResult<?> getUser() {
//
//        // 获取当前登录用户信息
//        LoginUser loginUser = SecurityUtil.getUser();
//
//        SysUserInfoDTO userInfo = sysUserProvider.getUserByUsername(loginUser.getAccount()).getData();
//        Map<String, Object> data = new HashMap<>(7);
//        data.put("username", loginUser.getAccount());
//        data.put("realName", userInfo.getSysUser().getRealName());
//        data.put("nickname", userInfo.getSysUser().getNickname());
//        data.put("avatar", userInfo.getSysUser().getAvatar());
//        data.put("departId", userInfo.getSysUser().getDeptId());
//        data.put("tenantId", userInfo.getTenantId());
//        data.put("roles", userInfo.getRoles());
//        data.put("permissions", userInfo.getPermissions());
//        // 存入redis,以用于查询权限使用
//        redisService.set(RedisKeyConstant.META_PERMISSION_PREFIX + loginUser.getAccount(), data);
//        return ApiResult.data(data);
//    }

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
