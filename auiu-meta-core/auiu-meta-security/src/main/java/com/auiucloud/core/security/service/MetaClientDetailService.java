package com.auiucloud.core.security.service;

import com.auiucloud.core.security.model.OauthClientDetails;

/**
 * @author dries
 * @date 2022/3/1
 */
public interface MetaClientDetailService {

    /**
     * 查询客户端详情
     * @param clientId 客户端ID
     * @return OauthClientDetails
     */
    OauthClientDetails loadClientByClientId(String clientId);

}
