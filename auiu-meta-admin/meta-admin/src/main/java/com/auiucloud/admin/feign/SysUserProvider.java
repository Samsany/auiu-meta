package com.auiucloud.admin.feign;

import com.auiucloud.admin.vo.UserInfoVO;
import com.auiucloud.admin.service.ISysUserService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 * @date 2022/4/9
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "用户远程调用")
public class SysUserProvider implements ISysUserProvider {

    private final ISysUserService sysUserService;

    @Override
    @GetMapping(ProviderConstant.PROVIDER_USER_USERNAME)
    @Log(value = "用户名查询用户", exception = "用户名查询用户请求失败")
    @Operation(summary = "用户用户名查询", description = "用户用户名查询")
    public ApiResult<UserInfoVO> getUserByUsername(String username) {
        return ApiResult.data(sysUserService.getUserInfoByUsername(username));
    }

}
