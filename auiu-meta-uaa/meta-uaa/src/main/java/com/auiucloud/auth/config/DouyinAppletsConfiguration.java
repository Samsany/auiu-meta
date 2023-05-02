package com.auiucloud.auth.config;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.auiucloud.auth.config.properties.DouyinAppletsProperties;
import com.auiucloud.auth.model.AppletConfig;
import com.auiucloud.auth.service.DouyinAppletsService;
import com.auiucloud.auth.service.impl.DouyinAppletsServiceImpl;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 抖音小程序配置
 *
 * @author dries
 **/
@Configuration
public class DouyinAppletsConfiguration {


    @Resource
    private DouyinAppletsProperties properties;

    private static final Map<String, DouyinAppletsService> DOUYIN_APPLETS_SERVICES = Maps.newHashMap();

    public static DouyinAppletsService getDouyinAppletService(String appId) {
        DouyinAppletsService douyinAppletsService = DOUYIN_APPLETS_SERVICES.get(appId);

        if (douyinAppletsService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appId));
        }

        return douyinAppletsService;
    }

    @PostConstruct
    public void init() {
        List<AppletConfig> configs = properties.getConfigs();
        if (CollUtil.isNotEmpty(configs)) {
            for (AppletConfig app : configs) {
                DouyinAppletsServiceImpl service = new DouyinAppletsServiceImpl(app);

                DOUYIN_APPLETS_SERVICES.put(app.getAppId(), service);
            }
        }
    }

}
