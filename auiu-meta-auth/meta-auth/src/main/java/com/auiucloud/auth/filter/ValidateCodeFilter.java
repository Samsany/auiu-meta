package com.auiucloud.auth.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.auth.config.properties.ValidateCodeProperties;
import com.auiucloud.auth.enums.ValidateCodeTypeEnum;
import com.auiucloud.auth.extension.captcha.ValidateCodeProcessorHolder;
import com.auiucloud.core.common.exception.CaptchaException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author dries
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final ValidateCodeProperties validateProperties;
    /**
     * 验证码校验失败处理器
     */
    // private final CodeAuthenticationFailureHandler onAuthenticationFailure;
    /**
     * 系统中的校验码处理器
     */
    private final ValidateCodeProcessorHolder validateCodeProcessorHolder;
    /**
     * 存放所有需要校验验证码的url
     */
    private final Map<String, ValidateCodeTypeEnum> urlMap = new HashMap<>();


    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        addUrlToMap(validateProperties.getImg().getUrls(), ValidateCodeTypeEnum.IMAGE);
        addUrlToMap(validateProperties.getSms().getUrls(), ValidateCodeTypeEnum.SMS);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (!request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            ValidateCodeTypeEnum type = getValidateCodeType(request);
            // MyRequestWrapper requestWrapper = new MyRequestWrapper(request);
            if (type != null) {
                log.info("校验请求(" + request.getRequestURI() + ")中的验证码,验证码类型" + type);
                try {
                    validateCodeProcessorHolder.findValidateCodeProcessor(type)
                            .validate();
                    log.info("验证码校验通过");
                } catch (CaptchaException e) {
                    // onAuthenticationFailure.onAuthenticationFailure(request, response, e);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }



    /**
     * 将系统中配置的需要校验验证码的URL根据校验的类型放入map
     */
    protected void addUrlToMap(List<String> urls, ValidateCodeTypeEnum type) {
        if (CollUtil.isNotEmpty(urls)) {
            urls.forEach(e -> urlMap.put(e, type));
        }
    }

    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null
     */
    private ValidateCodeTypeEnum getValidateCodeType(HttpServletRequest request) {
        ValidateCodeTypeEnum result = null;
        if (!StrUtil.equalsIgnoreCase(request.getMethod(), "get")) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    result = urlMap.get(url);
                }
            }
        }
        return result;
    }
}
