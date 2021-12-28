package com.auiucloud.rule.service.impl;

import com.auiucloud.core.redis.core.RedisService;
import com.auiucloud.rule.constant.RuleConstant;
import com.auiucloud.rule.model.BlackList;
import com.auiucloud.rule.service.IRuleCacheService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author dries
 * @date 2021/12/27
 */
public class RuleCacheServiceImpl implements IRuleCacheService {

    @Resource
    private RedisService redisService;

    @Override
    public Set<Object> getBlackList(String ip) {
        return redisService.sMembers(RuleConstant.getBlackListCacheKey(ip));
    }

    @Override
    public Set<Object> getBlackList() {
        return redisService.sMembers(RuleConstant.getBlackListCacheKey());
    }

    @Override
    public void setBlackList(BlackList blackList) {
        Gson gson = new Gson();
        String key = StringUtils.isNotBlank(blackList.getIp()) ? RuleConstant.getBlackListCacheKey(blackList.getIp())
                : RuleConstant.getBlackListCacheKey();
        redisService.set(key, gson.toJson(blackList));
    }

    @Override
    public void deleteBlackList(BlackList blackList) {
        Gson gson = new Gson();
        String key = StringUtils.isNotBlank(blackList.getIp()) ? RuleConstant.getBlackListCacheKey(blackList.getIp())
                : RuleConstant.getBlackListCacheKey();
        redisService.sRemove(key, gson.toJson(blackList));
    }

}
