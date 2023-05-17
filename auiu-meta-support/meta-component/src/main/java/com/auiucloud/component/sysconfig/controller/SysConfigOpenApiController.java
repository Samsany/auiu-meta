package com.auiucloud.component.sysconfig.controller;

import com.auiucloud.component.sysconfig.domain.AppletConfigProperties;
import com.auiucloud.component.sysconfig.domain.UserConfigProperties;
import com.auiucloud.component.sysconfig.service.ISysConfigService;
import com.auiucloud.component.sysconfig.service.ISysProtocolConfigService;
import com.auiucloud.component.sysconfig.vo.AppletSimpleConfigVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 **/
@Tag(name = "系统配置开放API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/system")
public class SysConfigOpenApiController {

    private final ISysConfigService sysConfigService;
    private final ISysProtocolConfigService sysProtocolConfigService;

    /**
     * 获取小程序配置
     */
    @Log(value = "系统配置", exception = "获取小程序配置请求异常")
    @GetMapping(value = "/applet-config")
    @Operation(summary = "获取小程序配置", description = "获取小程序配置")
    public ApiResult<?> getAppletConfigInfo(@RequestParam String appId) {
        AppletSimpleConfigVO appletSimpleConfigVO = new AppletSimpleConfigVO();
        AppletConfigProperties appletConfigProperties = sysConfigService.getAppletConfigProperties(appId);
        BeanUtils.copyProperties(appletConfigProperties, appletSimpleConfigVO);
        UserConfigProperties userConfigProperties = sysConfigService.getUserConfigProperties();
        BeanUtils.copyProperties(userConfigProperties, appletSimpleConfigVO);
        return ApiResult.data(appletSimpleConfigVO);
    }

    /**
     * 获取用户配置
     */
    @Log(value = "系统配置", exception = "获取用户配置请求异常")
    @GetMapping(value = "/user-config")
    @Operation(summary = "获取用户配置", description = "获取用户配置")
    public ApiResult<?> getUserConfigInfo() {
        return ApiResult.data(sysConfigService.getUserConfigProperties());
    }

    /**
     * 获取用户隐私协议
     */
    @Log(value = "系统配置", exception = "获取用户隐私协议详情请求异常")
    @GetMapping(value = "/privacy-agreement")
    @Operation(summary = "获取用户隐私协议详情", description = "获取用户隐私协议配置详情")
    public ApiResult<?> getPrivacyAgreementInfo() {
        return ApiResult.data(sysProtocolConfigService.getPrivacyAgreementInfo());
    }

    /**
     * 获取用户协议
     */
    @Log(value = "系统配置", exception = "获取用户协议配置详情请求异常")
    @GetMapping(value = "/user-agreement")
    @Operation(summary = "获取用户协议配置详情", description = "获取用户协议配置详情")
    public ApiResult<?> getUserAgreementInfo() {
        return ApiResult.data(sysProtocolConfigService.getUserAgreementInfo());
    }

    /**
     * 获取付费会员协议
     */
    @Log(value = "系统配置", exception = "获取付费会员协议详情请求异常")
    @GetMapping(value = "/paid-membership-agreement")
    @Operation(summary = "获取付费会员协议配置详情", description = "获取付费会员协议配置详情")
    public ApiResult<?> getPaidMembershipAgreementInfo() {
        return ApiResult.data(sysProtocolConfigService.getPaidMembershipAgreementInfo());
    }

    /**
     * 获取用户注销协议
     */
    @Log(value = "系统配置", exception = "获取用户注销协议详情请求异常")
    @GetMapping(value = "/user-write-off-agreement")
    @Operation(summary = "获取用户注销协议协议配置详情", description = "获取用户注销协议协议配置详情")
    public ApiResult<?> getUserWriteOffAgreementInfo() {
        return ApiResult.data(sysProtocolConfigService.getUserWriteOffAgreementInfo());
    }

    /**
     * 获取积分协议
     */
    @Log(value = "系统配置", exception = "获取积分协议详情请求异常")
    @GetMapping(value = "/point-agreement")
    @Operation(summary = "获取积分协议配置详情", description = "获取积分协议配置详情")
    public ApiResult<?> getPointAgreementInfo() {
        return ApiResult.data(sysProtocolConfigService.getPointAgreementInfo());
    }

    /**
     * 获取积分充值协议
     */
    @Log(value = "系统配置", exception = "获取积分充值协议详情请求异常")
    @GetMapping(value = "/point-recharge-agreement")
    @Operation(summary = "获取积分充值协议配置详情", description = "获取积分充值协议配置详情")
    public ApiResult<?> getPointRechargeAgreementInfo() {
        return ApiResult.data(sysProtocolConfigService.getPointRechargeAgreementInfo());
    }

    /**
     * 获取常见问题
     */
    @Log(value = "系统配置", exception = "获取常见问题详情请求异常")
    @GetMapping(value = "/faq")
    @Operation(summary = "获取常见问题配置详情", description = "获取常见问题配置详情")
    public ApiResult<?> getFAQInfo() {
        return ApiResult.data(sysProtocolConfigService.getFAQInfo());
    }
}
