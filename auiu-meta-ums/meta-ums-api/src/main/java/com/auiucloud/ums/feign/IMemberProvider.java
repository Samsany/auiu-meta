package com.auiucloud.ums.feign;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.feign.constant.FeignConstant;
import com.auiucloud.ums.dto.MemberInfoDTO;
import com.auiucloud.ums.dto.UserBrokerageChangeDTO;
import com.auiucloud.ums.dto.UserPointChangeDTO;
import com.auiucloud.ums.vo.MemberInfoVO;
import com.auiucloud.ums.vo.UserInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
     * @param userIds 用户名
     * @return ApiResult
     */
    @PostMapping(ProviderConstant.PROVIDER_USER_LIST_BY_IDS)
    ApiResult<List<MemberInfoVO>> getUserListByIds(@RequestBody List<Long> userIds);

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return ApiResult
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_USERNAME)
    ApiResult<MemberInfoVO> getUserByUsername(@RequestParam("username") String username);

    /**
     * 根据用户Id查询
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @GetMapping(ProviderConstant.PROVIDER_SIMPLE_USER_ID)
    ApiResult<UserInfoVO> getSimpleUserById(@RequestParam("openId") Long userId);

    /**
     * 根据用户名查询
     *
     * @param openId 用户唯一标识
     * @param source 第三方系统标识
     * @return ApiResult
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_OPENID)
    ApiResult<MemberInfoVO> getMemberByOpenId2Source(@RequestParam("openId") String openId, @RequestParam("source") String source);

    /**
     * 注册新会员
     *
     * @param memberInfoDTO 用户信息
     * @return ApiResult
     */
    @PostMapping(ProviderConstant.PROVIDER_USER_REGISTER_SOCIAL)
    ApiResult<MemberInfoDTO> registerMemberBySocial(@RequestBody MemberInfoDTO memberInfoDTO);

    /**
     * 用户积分扣减
     *
     * @param userPointChangeDTO 用户信息
     * @return ApiResult
     */
    @PostMapping(ProviderConstant.PROVIDER_USER_DECREASE_POINT)
    ApiResult<?> assignUserPoint(@RequestBody UserPointChangeDTO userPointChangeDTO);

    /**
     * 分配用户佣金
     *
     * @param userBrokerageChangeDTO 传递参数
     * @return ApiResult
     */
    @PostMapping(ProviderConstant.PROVIDER_USER_ASSIGN_BROKERAGE)
    ApiResult<?> assignUserBrokerage(@RequestBody UserBrokerageChangeDTO userBrokerageChangeDTO);
}
