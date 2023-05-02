package com.auiucloud.auth.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.auiucloud.auth.domain.MetaClientDetails;
import com.auiucloud.auth.service.OauthClientService;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.redis.core.RedisService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 自定义client表，并将数据缓存到redis，处理缓存优化
 * 当client数据变化时，同步至redis
 *
 * @author dries
 * @date 2022/3/1
 */
@Slf4j
@Setter
@Service
public class CustomClientDetailsService extends JdbcClientDetailsService {

    @Resource
    private DataSource dataSource;
    @Resource
    private RedisService redisService;
    @Resource
    private OauthClientService oauthClientService;
    @Resource
    private PasswordEncoder passwordEncoder;

    public CustomClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Bean
    @Primary
    public CustomClientDetailsService clientDetailService() {
        CustomClientDetailsService clientDetailsService = new CustomClientDetailsService(dataSource);
        clientDetailsService.setRedisService(redisService);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        clientDetailsService.setFindClientDetailsSql(Oauth2Constant.FIND_CLIENT_DETAIL_SQL);
        clientDetailsService.setSelectClientDetailsSql(Oauth2Constant.SELECT_CLIENT_DETAIL_SQL);
        return clientDetailsService;
    }

    /**
     * 从redis里读取ClientDetails
     *
     * @param clientId 客户端ID
     * @return ClientDetails
     * @throws InvalidClientException 非法客户端异常
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        MetaClientDetails clientDetails = (MetaClientDetails) redisService.get(RedisKeyConstant.cacheClientKey(clientId));
        if (ObjectUtil.isEmpty(clientDetails)) {
            return getCacheClient(clientId);
        }

        return buildClientDetails(clientDetails);
    }

    /**
     * 自定义语句查询，并将数据同步至redis
     *
     * @param clientId 客户端ID
     * @return ClientDetails
     */
    private ClientDetails getCacheClient(String clientId) {
        BaseClientDetails clientDetails = null;
        try {
            MetaClientDetails client = oauthClientService.loadClientByClientId(clientId);
            clientDetails = buildClientDetails(client);
            // clientDetails = super.loadClientByClientId(clientId);
            if (ObjectUtil.isNotEmpty(clientDetails)) {
                redisService.set(RedisKeyConstant.cacheClientKey(clientId), client);
                log.debug("Cache clientId:{}, clientDetails:{}", clientId, clientDetails);
                return clientDetails;
            } else {
                throw new NoSuchClientException("No client with requested id: " + clientId);
            }
        } catch (Exception e) {
            log.error("Exception for clientId:{}, message:{}", clientId, e.getMessage());
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }

    private BaseClientDetails buildClientDetails(MetaClientDetails client) {

        BaseClientDetails clientDetails = new BaseClientDetails(
                client.getClientId(),
                client.getResourceIds(),
                client.getScope(),
                client.getAuthorizedGrantTypes(),
                client.getAuthorities(),
                client.getWebServerRedirectUri()
        );
        clientDetails.setClientSecret(client.getClientSecret());
        clientDetails.isAutoApprove(client.getAutoApprove());
        clientDetails.setAccessTokenValiditySeconds(client.getAccessTokenValidity());
        clientDetails.setRefreshTokenValiditySeconds(client.getRefreshTokenValidity());

        return clientDetails;
    }
}
