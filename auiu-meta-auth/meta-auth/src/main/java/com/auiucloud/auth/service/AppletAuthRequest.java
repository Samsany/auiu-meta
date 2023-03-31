package com.auiucloud.auth.service;

import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.admin.feign.ISysUserProvider;
import com.auiucloud.admin.vo.UserInfoVO;
import com.auiucloud.auth.config.DouyinAppletsConfiguration;
import com.auiucloud.auth.domain.MetaClientDetails;
import com.auiucloud.auth.enums.Oauth2ClientTypeEnum;
import com.auiucloud.auth.model.AppletAuthCallback;
import com.auiucloud.auth.model.AppletCode2Session;
import com.auiucloud.auth.model.AppletUserInfo;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.redis.core.RedisService;
import com.auiucloud.ums.dto.MemberInfoDTO;
import com.auiucloud.ums.feign.IMemberProvider;
import com.auiucloud.ums.vo.MemberInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dries
 **/
@Slf4j
@Component
public class AppletAuthRequest {

    @Resource
    private ISysUserProvider sysUserProvider;
    @Resource
    private IMemberProvider memberProvider;
    @Resource
    private RedisService redisService;

    /**
     * 小程序用户登录授权
     *
     * @param source   小程序类型
     * @param callback 授权信息
     * @return ApiResult
     */
    public AppletUserInfo login(String source, AppletAuthCallback callback) {

        AuthenticationIdentityEnum anEnum = IBaseEnum.getEnumByName(source, AuthenticationIdentityEnum.class);
        switch (anEnum) {
            case DOUYIN_APPLET -> {
                // 获取Service
                DouyinAppletsService douyinAppletService = DouyinAppletsConfiguration.getDouyinAppletService(callback.getAppId());
                // 获取openid unionid
                AppletCode2Session code2Session = douyinAppletService.getCode2Session(callback.getCode());

                // 查询用户信息 判断客户端类型
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String clientId = authentication.getName();
                log.info("客户端：{}", clientId);
                MetaClientDetails clientDetails = (MetaClientDetails) redisService.get(RedisKeyConstant.cacheClientKey(clientId));

                Oauth2ClientTypeEnum clientTypeEnum = IBaseEnum.getEnumByValue(clientDetails.getClientType(), Oauth2ClientTypeEnum.class);
                switch (clientTypeEnum) {
                    case ADMIN -> {
                        ApiResult<UserInfoVO> apiResult = sysUserProvider.getSysUserByOpenId2Source(code2Session.getOpenid(), source);
                    }
                    case MEMBER -> {
                        ApiResult<MemberInfoVO> apiResult = memberProvider.getMemberByOpenId2Source(code2Session.getOpenid(), source);
                        AppletUserInfo userInfo = null;
                        if (apiResult != null) {
                            MemberInfoVO memberInfo = apiResult.getData();
                            if (apiResult.successful() && memberInfo != null) {
                                userInfo = AppletUserInfo.builder()
                                        .userId(memberInfo.getUserId())
                                        .account(memberInfo.getAccount())
                                        .openId(memberInfo.getOpenId())
                                        .nickName(memberInfo.getNickname())
                                        .avatarUrl(memberInfo.getAvatar())
                                        .gender(memberInfo.getGender())
                                        .city(memberInfo.getCity())
                                        .province(memberInfo.getProvince())
                                        .country(memberInfo.getCountry())
                                        .build();
                            } else if (apiResult.getCode() == ResultCode.USER_ERROR_A0201.getCode()) {
                                String encryptedData = callback.getEncryptedData();
                                String iv = callback.getIv();
                                String rawUserInfo = callback.getRawUserInfo();
                                String sessionKey = code2Session.getSession_key();

                                // 解密 encryptedData 获取用户信息
                                if (StrUtil.isNotBlank(encryptedData) && StrUtil.isNotBlank(iv)) {
                                    userInfo = douyinAppletService.getUserInfo(sessionKey, encryptedData, iv);
                                } else if (StrUtil.isNotBlank(rawUserInfo)){
                                    userInfo = AppletUserInfo.fromJson(rawUserInfo);
                                } else {
                                    userInfo = AppletUserInfo.builder()
                                            .nickName("#游客" + RandomUtil.randomNumbers(6))
                                            .gender(2)
                                            .build();
                                }
                                // 构建用户信息
                                MemberInfoDTO build = MemberInfoDTO.builder()
                                        .account(ObjectId.next())
                                        .openId(code2Session.getOpenid())
                                        .unionId(code2Session.getUnionid())
                                        .nickname(userInfo.getNickName())
                                        .avatar(userInfo.getAvatarUrl())
                                        .gender(userInfo.getGender())
                                        .country(userInfo.getCountry())
                                        .province(userInfo.getProvince())
                                        .city(userInfo.getCity())
                                        .language(userInfo.getLanguage())
                                        .registerSource(callback.getSource())
                                        .build();

                                memberProvider.registerMemberBySocial(build);

                                userInfo = userInfo
                                        .withAccount(build.getAccount())
                                        .withOpenId(build.getOpenId())
                                        .withUnionId(build.getUnionId());
                            }
                        }

                        return userInfo;
                    }
                }

            }
        }

        throw new InvalidGrantException("暂不支持的客户端类型");
    }

}
