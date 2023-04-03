package com.auiucloud.ums.feign;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.feign.constant.FeignConstant;
import com.auiucloud.ums.dto.MemberInfoDTO;
import com.auiucloud.ums.vo.MemberInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 会员远程调用接口
 *
 * @author dries
 **/
@FeignClient(value = FeignConstant.META_CLOUD_MEMBER)
public interface IMemberProvider {

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return ApiResult
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_USERNAME)
    ApiResult<MemberInfoVO> getUserByUsername(@RequestParam String username);

    /**
     * 根据用户名查询
     *
     * @param openId 用户唯一标识
     * @param source 第三方系统标识
     * @return ApiResult
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_OPENID)
    ApiResult<MemberInfoVO> getMemberByOpenId2Source(@RequestParam String openId, @RequestParam String source);

    /**
     * 注册新会员
     *
     * @param memberInfoDTO 用户信息
     * @return ApiResult
     */
    @PostMapping(ProviderConstant.PROVIDER_USER_REGISTER_SOCIAL)
    ApiResult<Boolean> registerMemberBySocial(@RequestBody MemberInfoDTO memberInfoDTO);
}
