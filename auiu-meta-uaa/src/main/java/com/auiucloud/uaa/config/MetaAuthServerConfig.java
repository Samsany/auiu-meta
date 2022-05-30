package com.auiucloud.uaa.config;

import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.security.config.AuthPropsConfig;
import com.auiucloud.core.security.model.MetaUser;
import com.auiucloud.core.security.service.impl.SingleLoginTokenServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.security.KeyPair;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证服务器配置
 * @author dries
 * @date 2022/2/27
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class MetaAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final KeyPair keyPair;
    private final UserDetailsService userDetailsService;
    private final RedisConnectionFactory redisConnectionFactory;
    private final AuthPropsConfig authPropsConfig;
    private final ClientDetailsService clientService;

    /**
     * 配置Token存储到Redis中
     */
    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 配置授权访问接入点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
        DefaultTokenServices tokenServices = createDefaultTokenServices();
        // token增强链
        TokenEnhancerChain chain = new TokenEnhancerChain();
        // 把jwt增强，与额外信息增强加入到增强链
        chain.setTokenEnhancers(List.of(tokenEnhancer(), jwtAccessTokenConverter()));
        tokenServices.setTokenEnhancer(chain);
        // 配置tokenServices参数
        addUserDetailsService(tokenServices);
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                // .tokenGranter(tokenGranter(endpoints, tokenServices))
                .tokenStore(redisTokenStore()) // 从 token 中读取特定字段构成 Authentication
                .tokenServices(tokenServices)
                .accessTokenConverter(jwtAccessTokenConverter());
    }

    /**
     * 配置 Jdbc 版本的 JdbcClientDetailsService
     *
     * @param clients 客户端
     * @throws Exception 异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
    }

    /**
     * 配置授权服务器的Token接入点
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 允许表单认证请求
                .allowFormAuthenticationForClients()
                // spel表达式 访问公钥端点（/auth/token_key）需要认证
                .tokenKeyAccess("isAuthenticated()")
                // spel表达式 访问令牌解析端点（/auth/check_token）需要认证
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * jwt token增强，添加额外信息
     *
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (oAuth2AccessToken, oAuth2Authentication) -> {
            // 添加额外信息的map
            final Map<String, Object> additionMessage = new HashMap<>(2);
            // 对于客户端鉴权模式，直接返回token
            if (oAuth2Authentication.getUserAuthentication() == null) {
                return oAuth2AccessToken;
            }
            // 获取当前登录的用户
            MetaUser user = (MetaUser) oAuth2Authentication.getUserAuthentication().getPrincipal();

            // 如果用户不为空 则把id放入jwt token中
            if (user != null) {
                additionMessage.put(Oauth2Constant.META_USER_ID, String.valueOf(user.getId()));
                additionMessage.put(Oauth2Constant.META_USER_NAME, user.getUsername());
                additionMessage.put(Oauth2Constant.META_AVATAR, user.getAvatar());
                additionMessage.put(Oauth2Constant.META_ROLE_IDS, String.valueOf(user.getRoleIds()));
                additionMessage.put(Oauth2Constant.META_TYPE, user.getType());
            }
            ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionMessage);
            return oAuth2AccessToken;
        };
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);
        return converter;
    }

    /**
     * 创建默认的tokenServices
     *
     * @return DefaultTokenServices
     */
    private DefaultTokenServices createDefaultTokenServices() {
        DefaultTokenServices tokenServices = new SingleLoginTokenServices(authPropsConfig.isSingleLogin());
        tokenServices.setTokenStore(redisTokenStore());
        // refresh_token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
        // 1.重复使用：access_token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
        // 2.非重复使用：access_token过期刷新时， refresh_token过期时间延续，在refresh_token有效期内刷新而无需失效再次登录
        // 支持刷新Token
        tokenServices.setSupportRefreshToken(Boolean.TRUE);
        tokenServices.setReuseRefreshToken(Boolean.FALSE);
        tokenServices.setClientDetailsService(clientService);
        addUserDetailsService(tokenServices);
        return tokenServices;
    }

    /**
     * 构建用户服务
     */
    private void addUserDetailsService(DefaultTokenServices tokenServices) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
            tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
        }
    }

}