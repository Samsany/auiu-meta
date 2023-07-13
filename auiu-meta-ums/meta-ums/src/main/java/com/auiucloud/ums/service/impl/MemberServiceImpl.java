package com.auiucloud.ums.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.component.cms.vo.UserStatisticalGalleryVO;
import com.auiucloud.component.feign.IUserGalleryProvider;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.exception.AuthException;
import com.auiucloud.core.common.model.IpAddress;
import com.auiucloud.core.common.model.dto.UpdatePasswordDTO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.IPUtil;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.common.utils.http.RequestHolder;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.douyin.config.AppletsConfiguration;
import com.auiucloud.core.douyin.service.DouyinAppletsService;
import com.auiucloud.ums.domain.Member;
import com.auiucloud.ums.domain.UserBrokerageRecord;
import com.auiucloud.ums.domain.UserIntegralRecord;
import com.auiucloud.ums.domain.UserLevelRecord;
import com.auiucloud.ums.dto.*;
import com.auiucloud.ums.enums.UserBrokerageEnums;
import com.auiucloud.ums.enums.UserPointEnums;
import com.auiucloud.ums.mapper.MemberMapper;
import com.auiucloud.ums.service.*;
import com.auiucloud.ums.vo.MemberInfoVO;
import com.auiucloud.ums.vo.UserInfoVO;
import com.auiucloud.ums.vo.UserRecommendVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【ums_member(会员表)】的数据库操作Service实现
 * @createDate 2023-03-28 10:11:41
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>
        implements IMemberService {

    private final PasswordEncoder passwordEncoder;
    private final IUserFollowerService userFollowerService;
    private final IUserLevelService userLevelService;
    private final IUserIntegralRecordService userIntegralRecordService;
    private final IUserBrokerageRecordService userBrokerageRecordService;

    @Resource
    private IUserGalleryProvider userGalleryProvider;

    /**
     * 会员分页列表
     *
     * @param search 查询参数
     * @param member 查询参数
     * @return PageUtils 分页对象
     */
    @Override
    public PageUtils listPage(Search search, Member member) {
        LambdaQueryWrapper<Member> queryWrapper = buildSearchParams(search, member);
        IPage<Member> page = this.page(PageUtils.getPage(search), queryWrapper);
        page.getRecords().parallelStream().forEach(this::hidePrivacyField);
        return new PageUtils(page);
    }

    @Override
    public List<UserRecommendVO> userRecommendList() {
        // 获取用户
        Long userId = SecurityUtil.getUserIdOrDefault();
        List<UserRecommendVO> userInfoVOList = baseMapper.selectUserRecommendList();
        Optional.ofNullable(userInfoVOList)
                .ifPresent(creatorVOS -> creatorVOS
                        .parallelStream()
                        .forEach(it -> {
                            // 设置是否关注该创作者
                            if (ObjectUtil.isNotNull(userId)) {
                                boolean isAttention = userFollowerService.checkedAttentionUser(userId, it.getId());
                                it.setIsAttention(isAttention);
                            }
                        }));
        return userInfoVOList;
    }

    @Override
    public PageUtils selectOpenApiUserPage(Search search) {
        // 获取用户
        Long userId = SecurityUtil.getUserIdOrDefault();
        Long total = baseMapper.selectOpenApiUserTotal(search, userId);
        Search newSearch = PageUtils.buildPage(search, Math.toIntExact(total));
        List<UserInfoVO> userList = Optional.ofNullable(baseMapper.selectOpenApiUserPage(newSearch, userId))
                .orElse(Collections.emptyList());
        // 构建用户附加信息
        userList.parallelStream().forEach(it -> buildUserAdditionalInfo(userId, it));
        return new PageUtils(total, search, userList);
    }

    @Override
    public UserInfoVO getSimpleUserById(Long userId) {
        Member member = this.getById(userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(member, userInfoVO);

        return userInfoVO;
    }

    @Override
    public UserInfoVO getUserInfoVOById(Long userId) {
        Member member = this.getById(userId);
        return getUserInfoVO(userId, member);
    }

    @Override
    public UserInfoVO queryUserInfoVOByInvitationCode(String invitationCode) {
        Member member = this.getOne(Wrappers.<Member>lambdaQuery().eq(Member::getInvitationCode, invitationCode));
        return getUserInfoVO(SecurityUtil.getUserId(), member);
    }

    @Override
    public PageUtils selectUserAttentionPage(Search search) {
        // 获取用户
        Long userId = SecurityUtil.getUserId();
        List<UserInfoVO> userInfoVOList = baseMapper.selectUserAttentionList(userId);
        Optional.ofNullable(userInfoVOList).ifPresent(creatorVOS -> creatorVOS.parallelStream().forEach(it -> {
            // 判断是否互关
            it.setIsAttention(Boolean.FALSE);
            if (ObjectUtil.isNotNull(userId)) {
                boolean isAttention = userFollowerService.checkedAttentionUser(it.getId(), userId);
                it.setIsAttention(isAttention);
            }
        }));
        return new PageUtils(search, userInfoVOList);
    }

    @Override
    public PageUtils selectUserFollowerPage(Search search) {
        // 获取用户
        Long userId = SecurityUtil.getUserId();
        List<UserInfoVO> userInfoVOList = baseMapper.selectUserFollowerList(userId);
        Optional.ofNullable(userInfoVOList).ifPresent(creatorVOS -> creatorVOS.parallelStream().forEach(it -> {
            // 判断是否互关
            it.setIsAttention(Boolean.FALSE);
            if (ObjectUtil.isNotNull(userId)) {
                boolean isAttention = userFollowerService.checkedAttentionUser(userId, it.getId());
                it.setIsAttention(isAttention);
            }
        }));
        return new PageUtils(search, userInfoVOList);
    }

    @Override
    public List<Member> selectMemberList(Search search, Member member) {
        LambdaQueryWrapper<Member> queryWrapper = buildSearchParams(search, member);
        List<Member> list = this.list(queryWrapper);
        // 对member部分字段进行隐藏显示，保护隐私
        Optional.ofNullable(list).orElse(Collections.emptyList())
                .parallelStream()
                .forEach(this::hidePrivacyField);
        return list;
    }

    @Override
    public List<MemberInfoVO> getMemberListByIds(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        List<Member> list = this.listByIds(userIds);
        // 对member部分字段进行隐藏显示，保护隐私
        return Optional.ofNullable(list).orElse(Collections.emptyList())
                .parallelStream()
                .map(this::getMemberInfoVO)
                .collect(Collectors.toList());
    }

    private void hidePrivacyField(Member member) {
        if (StrUtil.isNotBlank(member.getMobile())) {
            member.setMobile(StrUtil.hide(member.getMobile(), 3, member.getMobile().length() - 4));
        }
        if (StrUtil.isNotBlank(member.getCardId())) {
            member.setCardId(StrUtil.hide(member.getCardId(), 3, member.getCardId().length() - 4));
        }
        member.setPassword("");
    }

    @Override
    public Member getMemberInfoById(Long id) {
        // 对member部分字段进行隐藏显示，保护隐私
        Member member = this.getById(id);
        hidePrivacyField(member);
        return member;
    }

    /**
     * 根据用户username 查询会员信息
     *
     * @param username 账户
     * @return MemberInfoVO
     */
    @Override
    public MemberInfoVO getMemberByUsername(String username) {
        LambdaQueryWrapper<Member> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Member::getAccount, username);
        Member member = this.getOne(queryWrapper);
        MemberInfoVO memberInfoVO = new MemberInfoVO();
        BeanUtils.copyProperties(member, memberInfoVO);
        return memberInfoVO.withUserId(member.getId());
    }

    /**
     * 根据第三方信息(uuid+source)查询会员信息
     *
     * @param openId 用户唯一标识
     * @param source 第三方系统标识
     * @return MemberInfoVO
     */
    @Override
    public MemberInfoVO getMemberByOpenId2Source(String openId, String source) {
        Member member = this.baseMapper.getMemberByOpenId2Source(openId, source);
        if (member == null) {
            throw new ApiException(ResultCode.USER_ERROR_A0201);
        }
        MemberInfoVO memberInfoVO = new MemberInfoVO();
        BeanUtils.copyProperties(member, memberInfoVO);
        return memberInfoVO.withUserId(member.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveMember(RegisterMemberDTO registerMemberDTO) {
        Member member = new Member();
        BeanUtils.copyProperties(registerMemberDTO, member);
        if (!registerMemberDTO.getPassword().equals(registerMemberDTO.getConfirmPassword())) {
            throw new ApiException("两次输入密码不一致!");
        }
        member.setPassword(passwordEncoder.encode(registerMemberDTO.getConfirmPassword()));
        if (StrUtil.isNotBlank(member.getNickname())) {
            // 对mobile部分字段进行隐藏显示，保护隐私
            member.setNickname(StrUtil.hide(member.getMobile(), 3, member.getMobile().length() - 4));
        }
        return this.save(member);
    }

    /**
     * 用户注册
     *
     * @param memberInfoDTO 用户信息
     * @return Boolean
     */
    @Override
    public MemberInfoDTO registerMemberByApplet(MemberInfoDTO memberInfoDTO) {
        Member member = new Member();
        BeanUtils.copyProperties(memberInfoDTO, member);
        // 生成用户邀请码
        String code = RandomUtil.randomString(6);
        member.setInvitationCode(code);
        // todo 设置默认背景
        // 获取用户注册信息
        IpAddress ipAddress = IPUtil.getIpAddress();
        member.setRegisterIp(ipAddress.getIp());
        member.setRegisterAddress(ipAddress.getAddr());
        boolean result = this.save(member);
        if (result) {
            return memberInfoDTO.withId(member.getId());
        }
        throw new AuthException(ResultCode.USER_ERROR_A0100);
    }


    @Override
    public boolean updateMemberById(Member member) {
        return this.updateById(member);
    }

    @Override
    public ApiResult<?> updateAppletUserInfo(UpdateUserInfoDTO userInfoDTO) {

        // 文字内容安全效验
        try {
            String appId = RequestHolder.getHttpServletRequestHeader("appId");
            Integer loginType = SecurityUtil.getUser().getLoginType();
            if (loginType.equals(AuthenticationIdentityEnum.DOUYIN_APPLET.getValue())) {
                DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
                List<Integer> resultList = douyinAppletService.checkTextList(List.of(
                        userInfoDTO.getNickname(),
                        userInfoDTO.getRemark()
                ));
                if (resultList.size() > 0) {
                    return ApiResult.fail(ResultCode.USER_ERROR_A0431);
                }
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }

        LambdaUpdateWrapper<Member> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Member::getId, SecurityUtil.getUserId());
        updateWrapper.set(Member::getAvatar, userInfoDTO.getAvatar());
        updateWrapper.set(Member::getBgImg, userInfoDTO.getBgImg());
        updateWrapper.set(Member::getNickname, userInfoDTO.getNickname());
        updateWrapper.set(Member::getGender, userInfoDTO.getGender());
        updateWrapper.set(Member::getRemark, userInfoDTO.getRemark());
        return ApiResult.condition(this.update(updateWrapper));
    }

    @Override
    public boolean setUserStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<Member> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(Member::getStatus, updateStatusDTO.getStatus());
        updateWrapper.eq(Member::getId, updateStatusDTO.getId());
        return this.update(updateWrapper);
    }

    @Override
    public boolean setNewPassword(UpdatePasswordDTO updatePasswordDTO) {
        LambdaUpdateWrapper<Member> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.set(Member::getPassword, passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        queryWrapper.eq(Member::getId, updatePasswordDTO.getId());
        return this.update(queryWrapper);
    }

    @Override
    public boolean increaseUserPoint(Long userId, Integer integration) {
        return baseMapper.increaseUserPoint(userId, integration) > 0;
    }

    @Transactional
    @Override
    public boolean setUserIntegral(AdjustUserPointDTO adjustUserPoint) {
        Long userId = adjustUserPoint.getUserId();
        Integer integral = adjustUserPoint.getPoint();
        if (integral == CommonConstant.STATUS_NORMAL_VALUE) {
            return true;
        }
        Member member = this.getById(adjustUserPoint.getUserId());
        Integer oldIntegral = member.getIntegral();

        // 计算积分
        int newIntegral = 0;
        UserPointEnums.ChangeTypeEnum enumByValue = IBaseEnum.getEnumByValue(adjustUserPoint.getStatus(), UserPointEnums.ChangeTypeEnum.class);
        String title = "";
        switch (enumByValue) {
            case DECREASE -> {
                newIntegral = oldIntegral - integral;
                title = "系统扣除积分";
            }
            case INCREASE -> {
                newIntegral = oldIntegral + integral;
                title = "系统赠送积分";
            }
        }

        boolean result = this.saveMemberIntegral(userId, newIntegral);
        if (result) {
            // 保存用户积分变动记录
            UserIntegralRecord build = UserIntegralRecord.builder()
                    .userId(userId)
                    .title(title)
                    .integral(integral)
                    .balance(newIntegral)
                    .type(adjustUserPoint.getStatus())
                    .status(CommonConstant.STATUS_NORMAL_VALUE)
                    .frozenTime(0)
                    .thawTime(LocalDateTime.now())
                    .remark(adjustUserPoint.getRemark())
                    .build();
            userIntegralRecordService.save(build);
        }

        return result;
    }

    @Override
    public ApiResult<?> assignUserPoint(UserPointChangeDTO pointChangeDTO) {
        // 获取用户信息
        Long userId = pointChangeDTO.getUserId();
        Integer integral = pointChangeDTO.getIntegral();
        Member member = this.getById(userId);
        Integer oldIntegral = member.getIntegral();

        int newIntegral = 0;
        try {
            // 计算积分
            UserPointEnums.ChangeTypeEnum enumByValue = IBaseEnum.getEnumByValue(pointChangeDTO.getChangeType(), UserPointEnums.ChangeTypeEnum.class);
            switch (enumByValue) {
                case DECREASE -> {
                    if (integral > oldIntegral) {
                        return ApiResult.fail(ResultCode.USER_ERROR_A0160);
                    }
                    newIntegral = oldIntegral - integral;
                }
                case INCREASE -> {
                    newIntegral = oldIntegral + integral;
                }
            }
        } catch (Exception e) {
            log.error("积分分配异常，用户ID: {}, 积分来源: {}, 分配类型：{}, 异常信息：{}", userId, pointChangeDTO.getTitle(), pointChangeDTO.getChangeType(), e.getMessage());
            return ApiResult.fail("积分分配异常");
        }

        boolean result = this.saveMemberIntegral(userId, newIntegral);
        if (result) {
            // 保存用户积分变动记录
            UserIntegralRecord build = UserIntegralRecord.builder()
                    .userId(userId)
                    .title(pointChangeDTO.getTitle())
                    .integral(integral)
                    .balance(newIntegral)
                    .type(pointChangeDTO.getChangeType())
                    .status(pointChangeDTO.getStatus())
                    .frozenTime(0)
                    .thawTime(LocalDateTime.now())
                    .build();
            userIntegralRecordService.save(build);
        }

        return ApiResult.condition(result);
    }

    @Override
    public ApiResult<?> assignUserBrokerage(UserBrokerageChangeDTO userBrokerageChangeDTO) {
        // 获取用户信息
        Long userId = userBrokerageChangeDTO.getUserId();
        BigDecimal brokerage = userBrokerageChangeDTO.getBrokerage();

        Member member = this.getById(userId);
        BigDecimal brokeragePrice = member.getBrokeragePrice();

        BigDecimal total = null;
        try {
            // 计算佣金
            UserBrokerageEnums.ChangeTypeEnum enumByValue = IBaseEnum.getEnumByValue(userBrokerageChangeDTO.getChangeType(), UserBrokerageEnums.ChangeTypeEnum.class);
            switch (enumByValue) {
                case DECREASE -> {
                    total = brokeragePrice.subtract(brokerage);
                    if (total.compareTo(BigDecimal.ZERO) < 0) {
                        return ApiResult.fail(ResultCode.USER_ERROR_A0163);
                    }
                }
                case INCREASE -> {
                    total = brokeragePrice.add(brokerage);
                }
            }
        } catch (Exception e) {
            log.error("佣金分配异常，用户ID: {}, 佣金来源: {}, 分配类型：{}, 异常信息：{}", userId, userBrokerageChangeDTO.getTitle(), userBrokerageChangeDTO.getChangeType(), e.getMessage());
            return ApiResult.fail("佣金分配异常");
        }

        // 分配佣金
        boolean result = this.saveMemberBrokerage(userId, total);
        // 保存记录
        if (result) {
            // 保存用户积分使用记录
            UserBrokerageRecord build = UserBrokerageRecord.builder()
                    .userId(userId)
                    .linkId(userBrokerageChangeDTO.getLinkId())
                    .linkType(userBrokerageChangeDTO.getLinkType())
                    .title(userBrokerageChangeDTO.getTitle())
                    .price(brokerage)
                    .balance(total)
                    .type(userBrokerageChangeDTO.getChangeType())
                    .status(userBrokerageChangeDTO.getStatus())
                    .frozenTime(0)
                    .thawTime(LocalDateTime.now())
                    .build();
            userBrokerageRecordService.save(build);
        }

        return ApiResult.condition(result);
    }

    @Override
    public boolean saveMemberIntegral(Long userId, Integer total) {
        return this.update(Wrappers.<Member>lambdaUpdate()
                .set(Member::getIntegral, total)
                .eq(Member::getId, userId));
    }

    @Override
    public boolean saveMemberBrokerage(Long userId, BigDecimal total) {
        return this.update(Wrappers.<Member>lambdaUpdate()
                .set(Member::getBrokeragePrice, total)
                .eq(Member::getId, userId));
    }

    @Override
    public boolean checkUserPointQuantity(Integer point) {
        Member member = this.getById(SecurityUtil.getUserId());
        Integer integral = member.getIntegral();
        return integral >= point;
    }

    @Override
    public boolean checkUsernameExist(Member member) {
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Member::getAccount, member.getAccount());
        queryWrapper.ne(member.getId() != null, Member::getId, member.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean checkUserMobileExist(Member member) {
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Member::getMobile, member.getMobile());
        queryWrapper.ne(member.getId() != null, Member::getId, member.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean checkHasUserByGroupId(Long userGroupId) {
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.apply("find_in_set(" + userGroupId + ", group_ids)");
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean checkHasUserByTagId(Long tagId) {
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.apply("find_in_set(" + tagId + ", tag_ids)");
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean checkHasUserByLevelId(Long levelId) {
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Member::getLevelId, levelId);
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    /**
     * 组装LambdaQueryWrapper查询参数
     *
     * @param search 查询参数
     * @param member 查询参数
     * @return queryWrapper
     */
    private LambdaQueryWrapper<Member> buildSearchParams(Search search, Member member) {
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(search.getStartDate())) {
            queryWrapper.between(Member::getCreateTime, search.getStartDate(), search.getEndDate());
        }
        if (StringUtil.isNotBlank(search.getKeyword())) {
            queryWrapper.and(e -> e.like(Member::getAccount, search.getKeyword())
                    .or().like(Member::getMobile, search.getKeyword())
                    .or().like(Member::getNickname, search.getKeyword()));
        } else {
            queryWrapper.like(StringUtil.isNotBlank(member.getAccount()), Member::getAccount, member.getAccount());
            queryWrapper.like(StringUtil.isNotBlank(member.getMobile()), Member::getMobile, member.getMobile());
            queryWrapper.like(StringUtil.isNotBlank(member.getNickname()), Member::getNickname, member.getNickname());
        }

        queryWrapper.eq(ObjectUtil.isNotNull(search.getStatus()), Member::getStatus, search.getStatus());
        queryWrapper.orderByDesc(Member::getCreateTime);
        queryWrapper.orderByDesc(Member::getId);

        return queryWrapper;
    }

    /**
     * 用户信息转化
     *
     * @param member 用户信息
     * @return MemberInfoVO
     */
    public MemberInfoVO getMemberInfoVO(Member member) {
        MemberInfoVO memberInfoVO = new MemberInfoVO();
        BeanUtils.copyProperties(member, memberInfoVO);
        return memberInfoVO.withUserId(member.getId());
    }

    public UserInfoVO getUserInfoVO(Long userId, Member member) {
        UserInfoVO userInfoVO = null;
        if (ObjectUtil.isNotNull(member)) {
            userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(member, userInfoVO);

            // 判断是否为付费会员
            UserLevelRecord userLevelRecord = userLevelService.selectUserLevelRecordByUId(member.getId());
            userInfoVO.setIsPaidMember(Boolean.FALSE);
            if (userLevelRecord != null) {
                if (userLevelRecord.getExpiredTime().isAfter(LocalDateTime.now())) {
                    userInfoVO.setIsPaidMember(Boolean.TRUE);
                }
                userInfoVO.setMemberExpiredTime(userLevelRecord.getExpiredTime());
            }

            // 设置粉丝数
            long countUserFollower = userFollowerService.countUserFollower(member.getId());
            userInfoVO.setFollowerNum(countUserFollower);

            // 设置关注数
            long countUserAttention = userFollowerService.countUserAttention(member.getId());
            userInfoVO.setAttentionNum(countUserAttention);

            // 设置是否关注该创作者
            if (ObjectUtil.isNotNull(userId) && !Objects.equals(member.getId(), userId)) {
                boolean isAttention = userFollowerService.checkedAttentionUser(userId, member.getId());
                userInfoVO.setIsAttention(isAttention);
            }

            try {
                // 设置获赞数
                ApiResult<Long> countUserLikeResult = userGalleryProvider.countUserReceivedLikeNum(member.getId());
                if (countUserLikeResult != null && countUserLikeResult.successful()) {
                    userInfoVO.setReceivedLikeNum(countUserLikeResult.getData());
                }

                // 设置作品数
                ApiResult<Long> galleryApiResult = userGalleryProvider.countPublishUserGallery(member.getId());
                if (galleryApiResult != null && galleryApiResult.successful()) {
                    userInfoVO.setGalleryNum(galleryApiResult.getData());
                }
            } catch (Exception ignored) {
            }
        }

        return userInfoVO;
    }

    private void buildUserAdditionalInfo(Long userId, UserInfoVO userInfo) {
        // 判断是否为付费会员
        setUseIsPaidMember(userInfo);

        // 设置粉丝数
        setUserFollowerNum(userInfo);

        // 设置关注数 && 设置是否关注该创作者
        setUserAttentionInfo(userId, userInfo);

        // 设置获赞数
        setUserReceivedLikeNum(userInfo);

        // 设置作品数
        setUserGalleryInfo(userInfo);
    }

    // 设置作品数
    private void setUserGalleryInfo(UserInfoVO userInfo) {
        try {
            UserStatisticalGalleryVO userStatisticalGallery = userGalleryProvider.getUserStatisticalGalleryVOById(userInfo.getId()).getData();
            userInfo.setGalleryNum(userStatisticalGallery.getGalleryNum());
            List<UserStatisticalGalleryVO.GalleryInfo> galleryList = userStatisticalGallery.getGalleryList();
            userInfo.setGalleryList(
                    galleryList
                            .parallelStream()
                            .map(it -> UserInfoVO.GalleryInfo.builder()
                                    .id(it.getId())
                                    .thumbnail(it.getThumbnail())
                                    .build())
                            .toList()
            );
        } catch (Exception e) {
            log.error("获取用户作品信息异常: {}", e.getMessage());
        }
    }

    // 设置获赞数
    private void setUserReceivedLikeNum(UserInfoVO userInfo) {
        try {
            ApiResult<Long> countUserLikeResult = userGalleryProvider.countUserReceivedLikeNum(userInfo.getId());
            if (countUserLikeResult != null && countUserLikeResult.successful()) {
                userInfo.setReceivedLikeNum(countUserLikeResult.getData());
            }
        } catch (Exception e) {
            log.error("获取用户获赞数异常: {}", e.getMessage());
        }
    }

    // 判断是否为付费会员
    private void setUseIsPaidMember(UserInfoVO userInfo) {
        UserLevelRecord userLevelRecord = userLevelService.selectUserLevelRecordByUId(userInfo.getId());
        userInfo.setIsPaidMember(Boolean.FALSE);
        if (userLevelRecord != null) {
            if (userLevelRecord.getExpiredTime().isAfter(LocalDateTime.now())) {
                userInfo.setIsPaidMember(Boolean.TRUE);
            }
            userInfo.setMemberExpiredTime(userLevelRecord.getExpiredTime());
        }
    }

    // 设置粉丝数
    private void setUserFollowerNum(UserInfoVO userInfo) {
        long countUserFollower = userFollowerService.countUserFollower(userInfo.getId());
        userInfo.setFollowerNum(countUserFollower);
    }

    // 设置关注数 && 设置是否关注该创作者
    private void setUserAttentionInfo(Long userId, UserInfoVO userInfo) {
        // 设置关注数
        long countUserAttention = userFollowerService.countUserAttention(userInfo.getId());
        userInfo.setAttentionNum(countUserAttention);

        // 设置是否关注该创作者
        if (ObjectUtil.isNotNull(userId) && !Objects.equals(userInfo.getId(), userId)) {
            boolean isAttention = userFollowerService.checkedAttentionUser(userId, userInfo.getId());
            userInfo.setIsAttention(isAttention);
        }
    }
}




