package com.auiucloud.auth.service.impl;

import cn.hutool.core.lang.ObjectId;
import com.auiucloud.auth.domain.SocialUser;
import com.auiucloud.auth.domain.SocialUserAuth;
import com.auiucloud.auth.mapper.SocialUserMapper;
import com.auiucloud.auth.model.AppletAuthCallback;
import com.auiucloud.auth.model.AppletUserInfo;
import com.auiucloud.auth.service.ISocialUserAuthService;
import com.auiucloud.auth.service.ISocialUserService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.exception.AuthException;
import com.auiucloud.ums.dto.MemberInfoDTO;
import com.auiucloud.ums.feign.IMemberProvider;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【social_user(第三方用户表)】的数据库操作Service实现
 * @createDate 2023-02-26 22:46:21
 */
@Service
@RequiredArgsConstructor
public class SocialUserServiceImpl extends ServiceImpl<SocialUserMapper, SocialUser> implements ISocialUserService {

    private final ISocialUserAuthService socialUserAuthService;
    private final IMemberProvider memberProvider;

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public AppletUserInfo registerMemberBySocial(AppletUserInfo userInfo, AppletAuthCallback callback) {
        // 构建用户信息
        MemberInfoDTO build = MemberInfoDTO.builder()
                .account(ObjectId.next())
                .openId(callback.getOpenId())
                .unionId(callback.getUnionId())
                .nickname(userInfo.getNickName())
                .avatar(userInfo.getAvatarUrl())
                .gender(Integer.valueOf(userInfo.getGender()))
                .country(userInfo.getCountry())
                .province(userInfo.getProvince())
                .city(userInfo.getCity())
                .language(userInfo.getLanguage())
                .registerSource(callback.getSource())
                .build();
        // 注册用户
        ApiResult<MemberInfoDTO> result = memberProvider.registerMemberBySocial(build);
        MemberInfoDTO memberInfo = result.getData();
        if (result.successful() && memberInfo != null) {
            // 绑定用户关系
            SocialUser socialUser = SocialUser.builder()
                    .uuid(memberInfo.getOpenId())
                    .source(memberInfo.getRegisterSource())
                    .openId(memberInfo.getOpenId())
                    .unionId(memberInfo.getUnionId())
                    .build();
            this.bindMember2Social(memberInfo.getId(), socialUser);
            return userInfo
                    .withAccount(build.getAccount())
                    .withOpenId(build.getOpenId())
                    .withUnionId(build.getUnionId());
        }

        throw new AuthException(ResultCode.USER_ERROR_A0100);
    }

    @Override
    public Boolean bindMember2Social(Long userId, SocialUser socialUser) {
        boolean result = this.save(socialUser);
        if (result) {
            SocialUserAuth build = SocialUserAuth.builder()
                    .userId(userId)
                    .socialUserId(socialUser.getId())
                    .build();
            socialUserAuthService.save(build);
        }
        return result;
    }
}




