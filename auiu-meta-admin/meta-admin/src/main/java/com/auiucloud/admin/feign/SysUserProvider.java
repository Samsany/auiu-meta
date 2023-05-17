package com.auiucloud.admin.feign;

import com.auiucloud.admin.modules.system.service.ISysUserService;
import com.auiucloud.admin.modules.system.vo.UserInfoVO;
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
    @Operation(summary = "用户用户名查询", description = "用户用户名查询")
    public ApiResult<UserInfoVO> getUserByUsername(String username) {
        return ApiResult.data(sysUserService.getUserInfoByUsername(username));
    }

    @Override
    @Log(value = "第三方用户查询", exception = "第三方用户查询请求失败")
    @Operation(summary = "第三方用户查询", description = "第三方用户查询")
    public ApiResult<UserInfoVO> getSysUserByOpenId2Source(String openId, String source) {
        // 查询用户关联表
        return ApiResult.data(sysUserService.getSysUserByOpenId2Source(openId, source));
    }
}
