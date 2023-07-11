package com.auiucloud.ums.service;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.model.dto.UpdatePasswordDTO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.domain.Member;
import com.auiucloud.ums.dto.*;
import com.auiucloud.ums.vo.MemberInfoVO;
import com.auiucloud.ums.vo.UserInfoVO;
import com.auiucloud.ums.vo.UserRecommendVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author dries
 * @description 针对表【ums_member(会员表)】的数据库操作Service
 * @createDate 2023-03-28 10:11:41
 */
public interface IMemberService extends IService<Member> {

    PageUtils listPage(Search search, Member member);

    PageUtils selectOpenApiUserPage(Search search);

    PageUtils selectUserAttentionPage(Search search);

    PageUtils selectUserFollowerPage(Search search);

    List<Member> selectMemberList(Search search, Member member);

    List<MemberInfoVO> getMemberListByIds(List<Long> userIds);

    List<UserRecommendVO> userRecommendList();

    UserInfoVO getSimpleUserById(Long userId);

    UserInfoVO getUserInfoVOById(Long userId);

    UserInfoVO queryUserInfoVOByInvitationCode(String invitationCode);

    Member getMemberInfoById(Long id);

    /**
     * 根据用户username 查询会员信息
     *
     * @param username
     * @return MemberInfoVO
     */
    MemberInfoVO getMemberByUsername(String username);

    /**
     * 根据用户openId 查询会员信息
     *
     * @param uuid   用户唯一标识
     * @param source 第三方系统标识
     * @return MemberInfoVO
     */
    MemberInfoVO getMemberByOpenId2Source(String uuid, String source);

    /**
     * 用户注册
     *
     * @param memberInfoDTO 用户信息
     * @return Boolean
     */
    MemberInfoDTO registerMemberByApplet(MemberInfoDTO memberInfoDTO);

    boolean saveMember(RegisterMemberDTO member);

    boolean updateMemberById(Member member);

    ApiResult<?> updateAppletUserInfo(UpdateUserInfoDTO userInfoDTO);

    boolean setUserStatus(UpdateStatusDTO updateStatusDTO);

    boolean checkUsernameExist(Member member);

    boolean checkUserMobileExist(Member member);

    boolean checkHasUserByGroupId(Long userGroupId);

    boolean checkHasUserByTagId(Long tagId);

    boolean checkHasUserByLevelId(Long levelId);

    boolean setNewPassword(UpdatePasswordDTO updatePasswordDTO);

    /**
     * 增加积分
     *
     * @param userId      用户ID
     * @param integration 增加的积分
     * @return
     */
    boolean increaseUserPoint(Long userId, Integer integration);

    boolean setUserIntegral(AdjustUserPointDTO adjustUserPoint);

    ApiResult<?> assignUserPoint(UserPointChangeDTO pointChangeDTO);

    ApiResult<?> assignUserBrokerage(UserBrokerageChangeDTO userBrokerageChangeDTO);

    boolean saveMemberBrokerage(Long userId, BigDecimal total);

    boolean saveMemberIntegral(Long userId, Integer total);

    boolean checkUserPointQuantity(Integer point);

}
