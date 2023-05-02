package com.auiucloud.auth.service.impl;

import com.auiucloud.auth.domain.MetaClientDetails;
import com.auiucloud.auth.mapper.OauthClientMapper;
import com.auiucloud.auth.service.OauthClientService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author dries
 **/
@Service
public class OauthClientServiceImpl extends ServiceImpl<OauthClientMapper, MetaClientDetails> implements OauthClientService {

    /**
     * 查询客户端信息
     *
     * @param clientId 客户端ID
     * @return OauthClient
     */
    @Override
    public MetaClientDetails loadClientByClientId(String clientId) {
        LambdaQueryWrapper<MetaClientDetails> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MetaClientDetails::getClientId, clientId);
        return Optional.ofNullable(this.getOne(queryWrapper))
                .orElseThrow(() -> new NoSuchClientException("No client with requested id: " + clientId));
    }

}
