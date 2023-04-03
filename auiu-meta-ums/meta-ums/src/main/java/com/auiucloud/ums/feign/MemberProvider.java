package com.auiucloud.ums.feign;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.ums.dto.MemberInfoDTO;
import com.auiucloud.ums.service.IMemberService;
import com.auiucloud.ums.vo.MemberInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "会员远程调用")
public class MemberProvider implements IMemberProvider {

    private final IMemberService memberService;


    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return ApiResult
     */
    @Override
    @GetMapping(ProviderConstant.PROVIDER_USER_USERNAME)
    @Log(value = "用户名查询用户", exception = "用户名查询用户请求失败")
    @Operation(summary = "用户用户名查询", description = "用户用户名查询")
    public ApiResult<MemberInfoVO> getUserByUsername(String username) {
        return ApiResult.data(memberService.getMemberByUsername(username));
    }

    /**
     * 社交登录
     *
     * @param openId 用户唯一标识
     * @param source 第三方系统标识
     * @return ApiResult
     */
    @Override
    @GetMapping(ProviderConstant.PROVIDER_USER_OPENID)
    @Log(value = "社交登录", exception = "社交登录请求失败")
    @Operation(summary = "社交登录", description = "社交登录")
    public ApiResult<MemberInfoVO> getMemberByOpenId2Source(String openId, String source) {
        // 查询用户关联表
        return ApiResult.data(memberService.getMemberByOpenId2Source(openId, source));
    }

    /**
     * 社交用户注册
     *
     * @param memberInfoDTO 用户唯一标识
     * @return ApiResult
     */
    @Override
    @PostMapping(ProviderConstant.PROVIDER_USER_REGISTER_SOCIAL)
    @Log(value = "社交用户注册", exception = "社交用户注册请求失败")
    @Operation(summary = "社交用户注册", description = "社交用户注册")
    public ApiResult<Boolean> registerMemberBySocial(MemberInfoDTO memberInfoDTO) {
        return ApiResult.data(memberService.registerMemberByApplet(memberInfoDTO));
    }
}