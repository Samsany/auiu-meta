package com.auiucloud.ums.feign;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.ums.dto.MemberInfoDTO;
import com.auiucloud.ums.dto.UserBrokerageChangeDTO;
import com.auiucloud.ums.dto.UserPointChangeDTO;
import com.auiucloud.ums.service.IMemberService;
import com.auiucloud.ums.vo.MemberInfoVO;
import com.auiucloud.ums.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * 根据用户ID批量查询用户列表
     *
     * @param userIds 用户ID
     * @return ApiResult
     */
    @Override
    @Log(value = "用户ID批量查询用户列表", exception = "用户ID查询用户列表请求失败")
    @Operation(summary = "用户ID批量查询用户列表", description = "用户ID批量查询用户列表")
    public ApiResult<List<MemberInfoVO>> getUserListByIds(List<Long> userIds) {
        return ApiResult.data(memberService.getMemberListByIds(userIds));
    }

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
     * 根据用户Id查询
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @Override
    @Log(value = "用户名查询用户", exception = "用户名查询用户请求失败")
    @Operation(summary = "用户用户名查询", description = "用户用户名查询")
    public ApiResult<UserInfoVO> getSimpleUserById(Long userId) {
        return ApiResult.data(memberService.getSimpleUserById(userId));
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
    public ApiResult<MemberInfoDTO> registerMemberBySocial(MemberInfoDTO memberInfoDTO) {
        return ApiResult.data(memberService.registerMemberByApplet(memberInfoDTO));
    }

    /**
     * 用户积分扣减
     *
     * @param userPointChangeDTO 用户信息
     * @return ApiResult
     */
    @Override
    @Log(value = "用户积分扣减", exception = "用户积分扣减请求失败")
    @Operation(summary = "用户积分扣减", description = "用户积分扣减")
    public ApiResult<Boolean> decreaseUserPoint(@Validated @RequestBody UserPointChangeDTO userPointChangeDTO) {
        return ApiResult.data(memberService.decreaseUserPoint(userPointChangeDTO));
    }

    /**
     * 分配用户佣金
     *
     * @param userBrokerageChangeDTO 传递参数
     * @return ApiResult
     */
    @Override
    public ApiResult<Boolean> assignUserBrokerage(UserBrokerageChangeDTO userBrokerageChangeDTO) {
        return ApiResult.data(memberService.assignUserBrokerage(userBrokerageChangeDTO));
    }
}
