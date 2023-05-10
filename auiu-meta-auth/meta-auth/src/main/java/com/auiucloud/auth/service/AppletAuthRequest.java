package com.auiucloud.auth.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.admin.feign.ISysUserProvider;
import com.auiucloud.admin.modules.system.vo.UserInfoVO;
import com.auiucloud.auth.domain.MetaClientDetails;
import com.auiucloud.auth.enums.Oauth2ClientTypeEnum;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.common.exception.AuthException;
import com.auiucloud.core.common.utils.StringPool;
import com.auiucloud.core.douyin.config.AppletsConfiguration;
import com.auiucloud.core.douyin.model.AppletAuthCallback;
import com.auiucloud.core.douyin.model.AppletUserInfo;
import com.auiucloud.core.douyin.model.DyAppletCode2Session;
import com.auiucloud.core.douyin.service.DouyinAppletsService;
import com.auiucloud.core.redis.core.RedisService;
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
    private ISocialUserService socialUserService;
    @Resource
    private RedisService redisService;

    /**
     * 小程序用户登录授权
     *
     * @param callback 授权信息
     * @return ApiResult
     */
    public AppletUserInfo login(AppletAuthCallback callback) {

        AuthenticationIdentityEnum anEnum = IBaseEnum.getEnumByName(callback.getSource(), AuthenticationIdentityEnum.class);
        switch (anEnum) {
            case DOUYIN_APPLET -> {
                // 查询用户信息 判断客户端类型
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String clientId = authentication.getName();
                // 获取Service
                DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(callback.getAppId());
                DyAppletCode2Session code2Session = null;
                if (StrUtil.isBlank(callback.getOpenId())) {
                    // 获取openid unionid
                    code2Session = douyinAppletService.getCode2Session(callback.getCode());
                    callback.setOpenId(code2Session.getOpenid());
                    callback.setUnionId(code2Session.getOpenid());
                }
                log.debug("客户端：{}", clientId);
                MetaClientDetails clientDetails = (MetaClientDetails) redisService.get(RedisKeyConstant.cacheClientKey(clientId));

                Oauth2ClientTypeEnum clientTypeEnum = IBaseEnum.getEnumByValue(clientDetails.getClientType(), Oauth2ClientTypeEnum.class);
                switch (clientTypeEnum) {
                    // 管理端
                    case ADMIN -> {
                        ApiResult<UserInfoVO> apiResult = sysUserProvider.getSysUserByOpenId2Source(callback.getOpenId(), callback.getSource());
                    }
                    // 会员端
                    case MEMBER -> {
                        // 组装用户来源
                        String source = callback.getSource() + StringPool.AT + clientId;
                        ApiResult<MemberInfoVO> apiResult = memberProvider.getMemberByOpenId2Source(callback.getOpenId(), source);
                        AppletUserInfo userInfo = null;
                        MemberInfoVO memberInfo = apiResult.getData();
                        if (apiResult.successful() && memberInfo != null) {
                            userInfo = AppletUserInfo.builder()
                                    .userId(memberInfo.getUserId())
                                    .account(memberInfo.getAccount())
                                    .openId(memberInfo.getOpenId())
                                    .nickName(memberInfo.getNickname())
                                    .avatarUrl(memberInfo.getAvatar())
                                    .gender(String.valueOf(memberInfo.getGender()))
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
                            } else if (StrUtil.isNotBlank(rawUserInfo)) {
                                userInfo = AppletUserInfo.fromJson(rawUserInfo);
                            } else {
                                userInfo = AppletUserInfo.builder()
                                        .nickName(RandomUtil.randomString("#",6))
                                        .gender("2")
                                        .build();
                            }

                            // 注册用户
                            callback.setSource(source);
                            userInfo = socialUserService.registerMemberBySocial(userInfo, callback);
                        }

                        return userInfo;
                    }
                }
            }
            case WECHAT_APPLET -> {
                // 查询用户信息 判断客户端类型
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String clientId = authentication.getName();
                // 获取Service
                WxMaService wxMaService = AppletsConfiguration.getWechatAppletsService(callback.getAppId());
                WxMaUserService userService = wxMaService.getUserService();
                WxMaJscode2SessionResult session = null;
                if (StrUtil.isBlank(callback.getOpenId())) {
                    try {
                        // 获取openid unionid
                        session = userService.getSessionInfo(callback.getCode());
                        callback.setOpenId(session.getOpenid());
                        callback.setUnionId(session.getOpenid());
                    } catch (Exception e) {
                        throw new AuthException(e.getMessage());
                    }
                }
                log.debug("客户端：{}", clientId);
                MetaClientDetails clientDetails = (MetaClientDetails) redisService.get(RedisKeyConstant.cacheClientKey(clientId));

                Oauth2ClientTypeEnum clientTypeEnum = IBaseEnum.getEnumByValue(clientDetails.getClientType(), Oauth2ClientTypeEnum.class);
                switch (clientTypeEnum) {
                    // 管理端
                    case ADMIN -> {
                        ApiResult<UserInfoVO> apiResult = sysUserProvider.getSysUserByOpenId2Source(callback.getOpenId(), callback.getSource());
                    }
                    // 会员端
                    case MEMBER -> {
                        // 组装用户来源
                        String source = callback.getSource() + StringPool.AT + clientId;
                        ApiResult<MemberInfoVO> apiResult = memberProvider.getMemberByOpenId2Source(callback.getOpenId(), source);
                        AppletUserInfo userInfo = null;
                        MemberInfoVO memberInfo = apiResult.getData();
                        if (apiResult.successful() && memberInfo != null) {
                            userInfo = AppletUserInfo.builder()
                                    .userId(memberInfo.getUserId())
                                    .account(memberInfo.getAccount())
                                    .openId(memberInfo.getOpenId())
                                    .nickName(memberInfo.getNickname())
                                    .avatarUrl(memberInfo.getAvatar())
                                    .gender(String.valueOf(memberInfo.getGender()))
                                    .city(memberInfo.getCity())
                                    .province(memberInfo.getProvince())
                                    .country(memberInfo.getCountry())
                                    .build();
                        } else if (apiResult.getCode() == ResultCode.USER_ERROR_A0201.getCode()) {
                            String encryptedData = callback.getEncryptedData();
                            String iv = callback.getIv();
                            String rawUserInfo = callback.getRawUserInfo();
                            String sessionKey = session.getSessionKey();

                            // 解密 encryptedData 获取用户信息
                            if (StrUtil.isNotBlank(encryptedData) && StrUtil.isNotBlank(iv)) {
                                WxMaUserInfo maUserInfo = userService.getUserInfo(sessionKey, encryptedData, iv);
                                userInfo = AppletUserInfo.fromJson(JSONUtil.toJsonStr(maUserInfo));
                            } else if (StrUtil.isNotBlank(rawUserInfo)) {
                                userInfo = AppletUserInfo.fromJson(rawUserInfo);
                            } else {
                                userInfo = AppletUserInfo.builder()
                                        .gender("2")
                                        .build();
                            }

                            userInfo.setNickName(RandomUtil.randomString("#",6));
                            // 注册用户
                            callback.setSource(source);
                            userInfo = socialUserService.registerMemberBySocial(userInfo, callback);
                        }

                        return userInfo;
                    }
                }
            }
        }

        throw new InvalidGrantException("暂不支持的客户端类型");
    }

}
