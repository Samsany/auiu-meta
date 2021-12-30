package com.auiucloud.gateway.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.auiucloud.core.common.api.ApiResponse;
import com.auiucloud.core.common.utils.IPUtil;
import com.auiucloud.core.common.utils.ResponseUtil;
import com.auiucloud.gateway.service.ISafeRuleService;
import com.auiucloud.core.rule.constant.RuleConstant;
import com.auiucloud.core.rule.model.BlackList;
import com.auiucloud.core.rule.service.IRuleCacheService;
import com.google.common.base.Stopwatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author dries
 * @date 2021/12/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SafeRuleServiceImpl implements ISafeRuleService {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final IRuleCacheService ruleCacheService;

    @Override
    public Mono<Void> filterBlackList(ServerWebExchange exchange) {
        // Stopwatch 计时指定代码段的运行时间以及汇总这个运行时间
        Stopwatch stopwatch = Stopwatch.createStarted();
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        try {
            URI originUri = getOriginRequestUri(exchange);
            String requestIp = IPUtil.getServerHttpRequestIpAddress(request);
            String requestMethod = request.getMethodValue();
            AtomicBoolean forbid = new AtomicBoolean(false);
            // 从缓存中获取黑名单信息
            Set<Object> blackLists = ruleCacheService.getBlackList(requestIp);
            blackLists.addAll(ruleCacheService.getBlackList());
            // 检查是否在黑名单中
            checkBlackLists(forbid, blackLists, originUri, requestMethod);
            log.debug("黑名单检查完成 - {}", stopwatch.stop());
            if (forbid.get()) {
                log.info("属于黑名单地址 - {}", originUri.getPath());
                return ResponseUtil.webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE,
                        HttpStatus.NOT_ACCEPTABLE, ApiResponse.fail(HttpStatus.NOT_ACCEPTABLE.value(), "已列入黑名单，访问受限"));
            }
        } catch (Exception e) {
            log.error("黑名单检查异常: {} - {}", e.getMessage(), stopwatch.stop());
        }

        return null;
    }

    /**
     * 获取网关请求URI
     *
     * @param exchange ServerWebExchange
     * @return URI
     */
    private URI getOriginRequestUri(ServerWebExchange exchange) {
        return exchange.getRequest().getURI();
    }

    /**
     * 检查是否满足黑名单的条件
     *
     * @param forbid        是否黑名单判断
     * @param blackLists    黑名列表
     * @param uri           资源
     * @param requestMethod 请求方法
     */
    private void checkBlackLists(AtomicBoolean forbid, Set<Object> blackLists, URI uri, String requestMethod) {
        DateTime currentTime = DateUtil.date();
        for (Object bl : blackLists) {
            BlackList blackList = JSONObject.parseObject(bl.toString(), BlackList.class);
            if (antPathMatcher.match(blackList.getRequestUri(), uri.getPath()) && RuleConstant.BLACKLIST_OPEN.equals(blackList.getStatus())) {
                if (RuleConstant.ALL.equalsIgnoreCase(blackList.getRequestMethod())
                        || StringUtils.equalsIgnoreCase(requestMethod, blackList.getRequestMethod())) {

                    if (StringUtil.isNotBlank(blackList.getStartTime()) && StringUtil.isNotBlank(blackList.getEndTime())) {
                        DateTime startTime = DateUtil.parse(blackList.getStartTime());
                        DateTime endTime = DateUtil.parse(blackList.getEndTime());
                        if (DateUtil.isIn(currentTime, startTime, endTime)) {
                            forbid.set(Boolean.TRUE);
                        }
                    } else {
                        forbid.set(Boolean.TRUE);
                    }
                }
            }
            if (forbid.get()) {
                break;
            }
        }
    }
}
