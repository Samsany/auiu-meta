package com.auiucloud.rule.config;

import com.auiucloud.rule.service.IRuleCacheService;
import com.auiucloud.rule.service.impl.RuleCacheServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 规则配置
 *
 * @author dries
 * @date 2021/12/27
 */
@Configuration
public class RuleConfiguration {

    @Bean
    public IRuleCacheService ruleCacheService() {
        return new RuleCacheServiceImpl();
    }

}
