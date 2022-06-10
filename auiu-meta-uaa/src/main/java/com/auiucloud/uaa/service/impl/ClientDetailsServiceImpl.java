package com.auiucloud.uaa.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.redis.core.RedisService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
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
public class ClientDetailsServiceImpl extends JdbcClientDetailsService {

    @Resource
    private DataSource dataSource;
    @Resource
    private RedisService redisService;
    @Resource
    private PasswordEncoder passwordEncoder;

    public ClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Bean
    @Primary
    public ClientDetailsServiceImpl clientDetailService() {
        ClientDetailsServiceImpl clientDetailsService = new ClientDetailsServiceImpl(dataSource);
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
        ClientDetails clientDetails = (ClientDetails) redisService.get(clientKey(clientId));
        if (ObjectUtil.isEmpty(clientDetails)) {
            clientDetails = getCacheClient(clientId);
        }
        return clientDetails;
    }

    /**
     * 自定义语句查询，并将数据同步至redis
     *
     * @param clientId 客户端ID
     * @return ClientDetails
     */
    private ClientDetails getCacheClient(String clientId) {
        ClientDetails clientDetails = null;
        try {
            clientDetails = super.loadClientByClientId(clientId);
            if (ObjectUtil.isNotEmpty(clientDetails)) {
                redisService.set(clientKey(clientId), clientDetails);
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

    private String clientKey(String clientId) {
        return Oauth2Constant.META_CLIENT_TABLE + ":" + clientId;
    }
}
