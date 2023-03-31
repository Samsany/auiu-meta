package com.auiucloud.admin.controller;

import com.auiucloud.admin.service.ISysUserService;
import com.auiucloud.admin.vo.UserInfoVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.model.LoginUser;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.redis.core.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dries
 **/
@Slf4j
@RestController
@Tag(name = "认证管理")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final RedisService redisService;
    private final ISysUserService sysUserService;

    @Log(value = "用户信息", exception = "用户信息请求异常")
    @Operation(summary = "用户信息", description = "用户信息")
    @GetMapping("/user/info")
    public ApiResult<?> getUser() {

        // 获取当前登录用户信息
        LoginUser loginUser = SecurityUtil.getUser();
        // 获取用户信息
        UserInfoVO userInfo = sysUserService.getUserInfoByUsername(loginUser.getAccount());

        Map<String, Object> data = new HashMap<>(7);
        data.put("userId", userInfo.getUserId());
        data.put("username", loginUser.getAccount());
        data.put("realName", userInfo.getSysUser().getRealName());
        data.put("nickname", userInfo.getSysUser().getNickname());
        data.put("avatar", userInfo.getSysUser().getAvatar());
        data.put("departId", userInfo.getSysUser().getDeptId());
        data.put("tenantId", userInfo.getTenantId());
        data.put("roles", userInfo.getRoles());
        data.put("permissions", userInfo.getPermissions());
        // 存入redis,以用于查询权限使用
        // redisService.set(RedisKeyConstant.META_PERMISSION_PREFIX + loginUser.getAccount(), data);
        return ApiResult.data(data);
    }

}
