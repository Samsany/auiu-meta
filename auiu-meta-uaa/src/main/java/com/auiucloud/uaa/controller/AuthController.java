package com.auiucloud.uaa.controller;

import com.auiucloud.admin.feign.ISysUserProvider;
import com.auiucloud.core.common.api.ApiResult;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 * @createDate 2022-5-31 16:16
 */
@RestController
@Api(tags = "认证管理")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final ISysUserProvider sysUserProvider;
//
//    private final ISysRolePermissionProvider sysRolePermissionProvider;

    @GetMapping("/test")
    public ApiResult<?> testAuth() {
        return sysUserProvider.getUserByUsername("admin");
    }

}
