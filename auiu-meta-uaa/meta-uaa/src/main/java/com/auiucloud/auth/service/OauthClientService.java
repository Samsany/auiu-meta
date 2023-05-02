package com.auiucloud.auth.service;

import com.auiucloud.auth.domain.MetaClientDetails;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author dries
 **/
public interface OauthClientService extends IService<MetaClientDetails> {

    /**
     * 查询客户端信息
     *
     * @param clientId 客户端ID
     * @return OauthClient
     */
    MetaClientDetails loadClientByClientId(String clientId);

}
