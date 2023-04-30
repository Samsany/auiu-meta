package com.auiucloud.ums.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.auth.feign.ISocialProvider;
import com.auiucloud.component.feign.IUserGalleryProvider;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.exception.AuthException;
import com.auiucloud.core.common.model.IpAddress;
import com.auiucloud.core.common.model.dto.UpdatePasswordDTO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.IPUtil;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.domain.Member;
import com.auiucloud.ums.dto.MemberInfoDTO;
import com.auiucloud.ums.dto.RegisterMemberDTO;
import com.auiucloud.ums.mapper.MemberMapper;
import com.auiucloud.ums.service.IMemberService;
import com.auiucloud.ums.service.IUserFollowerService;
import com.auiucloud.ums.vo.MemberInfoVO;
import com.auiucloud.ums.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>
        implements IMemberService {

    private final PasswordEncoder passwordEncoder;
    private final ISocialProvider socialProvider;
    private final IUserFollowerService userFollowerService;
    private final IUserGalleryProvider userGalleryProvider;

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
    public List<UserInfoVO> userRecommendList() {
        // 获取用户
        Long userId = SecurityUtil.getUserIdOrDefault();
        List<UserInfoVO> userInfoVOList = baseMapper.selectUserRecommendList(userId);
        Optional.ofNullable(userInfoVOList).ifPresent(creatorVOS -> creatorVOS.parallelStream().forEach(it -> {
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
        LambdaQueryWrapper<Member> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StrUtil.isNotBlank(search.getKeyword()), Member::getInvitationCode, search.getKeyword());
        queryWrapper.ne(ObjectUtil.isNotNull(userId), Member::getId, userId);
        // todo 用户排序逻辑待完善
        queryWrapper.orderByDesc(Member::getCreateTime);
        IPage<Member> page = this.page(PageUtils.getPage(search), queryWrapper);
        if (page.getRecords().size() > 0) {
            page.convert(it -> getUserInfoVO(userId, it));
        }

        return new PageUtils(page);
    }

    @Override
    public UserInfoVO getUserInfoVOById(Long userId) {
        Member member = this.getById(userId);
        return getUserInfoVO(userId, member);
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
        String pro = StrUtil.isBlank(ipAddress.getPro()) ? "" : ipAddress.getPro();
        String city = StrUtil.isBlank(ipAddress.getCity()) ? "" : ipAddress.getCity();
        member.setRegisterIp(ipAddress.getIp());
        member.setRegisterAddress(pro + city + ipAddress.getRegion());
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
        if (StringUtil.isNotBlank(search.getStartDate())) {
            queryWrapper.between(Member::getCreateTime, search.getStartDate(), search.getEndDate());
        }
        if (StringUtil.isNotBlank(search.getKeyword())) {
            queryWrapper.and(e -> e.like(Member::getAccount, search.getKeyword())
                    .like(Member::getMobile, search.getKeyword())
                    .like(Member::getNickname, search.getKeyword()));
        } else {
            queryWrapper.like(StringUtil.isNotBlank(member.getAccount()), Member::getAccount, member.getAccount());
            queryWrapper.like(StringUtil.isNotBlank(member.getMobile()), Member::getMobile, member.getMobile());
            queryWrapper.like(StringUtil.isNotBlank(member.getNickname()), Member::getNickname, member.getNickname());
        }

        queryWrapper.eq(ObjectUtil.isNotNull(search.getStatus()), Member::getStatus, search.getStatus());
        queryWrapper.orderByDesc(Member::getCreateTime);

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
        UserInfoVO build = UserInfoVO.builder()
                .id(member.getId())
                .account(member.getAccount())
                .nickname(member.getNickname())
                .avatar(member.getAvatar())
                .bgImg(member.getBgImg())
                .invitationCode(member.getInvitationCode())
                .remark(member.getRemark())
                .build();

        // 设置粉丝数
        long countUserFollower = userFollowerService.countUserFollower(member.getId());
        build.setFollowerNum(countUserFollower);

        // 设置关注数
        long countUserAttention = userFollowerService.countUserAttention(member.getId());
        build.setAttentionNum(countUserAttention);

        // 设置是否关注该创作者
        if (ObjectUtil.isNotNull(userId) && !Objects.equals(member.getId(), userId)) {
            boolean isAttention = userFollowerService.checkedAttentionUser(userId, member.getId());
            build.setIsAttention(isAttention);
        }

        try {
            // 设置作品数
            ApiResult<Long> galleryApiResult = userGalleryProvider.countUserGallery(member.getId());
            if (galleryApiResult != null && galleryApiResult.successful()) {
                build.setGalleryNum(galleryApiResult.getData());
            }
        } catch (Exception ignored) {
        }

        return build;
    }
}




