package com.auiucloud.ums.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.redis.core.RedisService;
import com.auiucloud.ums.service.IMemberService;
import com.auiucloud.ums.vo.MemberInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final IMemberService memberService;

    @Log(value = "用户信息", exception = "用户信息请求异常")
    @Operation(summary = "用户信息", description = "用户信息")
    @GetMapping("/user/info")
    public ApiResult<?> getUser() {
        // 获取当前登录用户信息
        MemberInfoVO userInfo = memberService.getMemberByUsername(SecurityUtil.getUsername());
        return ApiResult.data(userInfo);
    }

}
