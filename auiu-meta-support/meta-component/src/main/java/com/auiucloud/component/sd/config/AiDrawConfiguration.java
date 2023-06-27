package com.auiucloud.component.sd.config;

import com.auiucloud.component.sd.component.AiDrawFactory;
import com.auiucloud.component.sd.domain.SdConfigProperties;
import com.auiucloud.component.sd.service.ISdConfigService;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dries
 **/
@Configuration
public class AiDrawConfiguration {

    @Resource
    private ISdConfigService sdConfigService;

    private static final Map<String, AiDrawFactory> AI_DRAW_FACTORYS = new HashMap<>();

    public static AiDrawFactory getAiDrawFactory(String sdId) {
        AiDrawFactory aiDrawFactory = AI_DRAW_FACTORYS.get(sdId);

        if (aiDrawFactory == null) {
            throw new IllegalArgumentException(String.format("未找到对应sd=[%s]的配置，请核实！", sdId));
        }

        return aiDrawFactory;
    }

    @PostConstruct
    public void init() {
        // 加载配置文件
        List<SdConfigProperties> propertiesList = sdConfigService.selectSdConfigList();
        for (SdConfigProperties properties : propertiesList) {
            AiDrawFactory aiDrawFactory = new AiDrawFactory();
            aiDrawFactory.setAppId(properties.getAppId());
            aiDrawFactory.setAppSecret(properties.getAppSecret());
            aiDrawFactory.setUrl(properties.getUrl());
            AI_DRAW_FACTORYS.put(properties.getCode(), aiDrawFactory);
        }
    }

}
