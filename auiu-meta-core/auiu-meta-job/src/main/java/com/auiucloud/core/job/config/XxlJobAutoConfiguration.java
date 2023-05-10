package com.auiucloud.core.job.config;

import com.auiucloud.core.job.properties.XxlExecutorProperties;
import com.auiucloud.core.job.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * @author dries
 **/
@Configuration(proxyBeanMethods = false)
@EnableAutoConfiguration
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobAutoConfiguration {

    /**
     * 服务名称 包含 XXL_JOB_ADMIN 则说明是 Admin
     */
    private static final String XXL_JOB_ADMIN = "meta-job-admin";

    /**
     * 配置xxl-job 执行器，提供自动发现 xxl-job-admin 能力
     *
     * @param xxlJobProperties xxl 配置
     * @param environment      环境变量
     * @param discoveryClient  注册发现客户端
     * @return
     */
    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties xxlJobProperties, Environment environment,
                                                     DiscoveryClient discoveryClient) {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        XxlExecutorProperties executor = xxlJobProperties.getExecutor();
        // 应用名默认为服务名
        String appName = executor.getAppName();
        if (!StringUtils.hasText(appName)) {
            appName = environment.getProperty("spring.application.name");
        }
        String accessToken = environment.getProperty("xxl.job.accessToken");
        if (!StringUtils.hasText(accessToken)) {
            accessToken = executor.getAccessToken();
        }

        xxlJobSpringExecutor.setAppname(appName);
        xxlJobSpringExecutor.setAddress(executor.getAddress());
        xxlJobSpringExecutor.setIp(executor.getIp());
        xxlJobSpringExecutor.setPort(executor.getPort());
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(executor.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());

        // 如果配置为空则获取注册中心的服务列表
        if (!StringUtils.hasText(xxlJobProperties.getAdmin().getAddresses())) {
            String serverList = discoveryClient.getServices().stream().filter(s -> s.contains(XXL_JOB_ADMIN))
                    .flatMap(s -> discoveryClient.getInstances(s).stream()).map(instance -> String
                            .format("http://%s:%s/%s", instance.getHost(), instance.getPort(), XXL_JOB_ADMIN))
                    .collect(Collectors.joining(","));
            xxlJobSpringExecutor.setAdminAddresses(serverList);
        } else {
            xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
        }

        return xxlJobSpringExecutor;
    }

}
