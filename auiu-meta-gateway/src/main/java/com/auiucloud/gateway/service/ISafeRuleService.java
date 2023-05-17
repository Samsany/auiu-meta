package com.auiucloud.gateway.service;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author dries
 * @date 2021/12/28
 */
public interface ISafeRuleService {

    /**
     * 黑名单过滤
     *
     * @param exchange
     * @return
     */
    Mono<Void> filterBlackList(ServerWebExchange exchange);

}
