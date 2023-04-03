package com.auiucloud.monitor.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.discovery.NacosWatch;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author dries
 **/
@Configuration
public class SecurityConfiguration {

    private final String adminContextPath;

    public SecurityConfiguration(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        http
                .authorizeHttpRequests((auth) -> auth
                        .antMatchers(adminContextPath + "/instances").permitAll()
                        .antMatchers(adminContextPath + "/actuator/**").permitAll()
                        .antMatchers(adminContextPath + "/assets/**").permitAll()
                        .antMatchers(adminContextPath + "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage(adminContextPath + "/login")
                        .successHandler(successHandler)
                )
                .logout(logout -> logout.logoutUrl(adminContextPath + "/logout"))
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(header -> header.frameOptions().disable())
        ;
        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "spring.cloud.nacos.discovery.watch.enabled", matchIfMissing = true)
    public NacosWatch nacosWatch(NacosServiceManager nacosServiceManager,
                                 NacosDiscoveryProperties properties,
                                 ObjectProvider<ThreadPoolTaskScheduler> taskScheduler) {
        return new NacosWatch(nacosServiceManager, properties, taskScheduler);
    }

    @Bean
    public CorsWebFilter corsFilter() {

        // 1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 允许跨越发送cookie
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        // 放行全部原始头信息
        config.addAllowedHeader("*");
        config.addExposedHeader("X-Authenticate");
        // 允许所有请求方法跨域调用
        config.addAllowedMethod("*");
        // config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 设置访问时间
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }


}
