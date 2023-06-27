package com.auiucloud.component.translate.config;

import com.auiucloud.component.translate.service.ITranslateConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author dries
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class TranslateConfigCache {

    private final ITranslateConfigService translationService;


    @PostConstruct
    public void init() {
        translationService.clearTranslation();
        // 加载OSS配置文件
        translationService.getTranslationProperties();
    }

    @PreDestroy
    public void destroy() {
        // 系统运行结束
        translationService.clearTranslation();
    }

    @Scheduled(cron = "0 0 0/2 * * ?")
    public void taskInit() {
        // 每2小时执行一次缓存
        init();
    }
}
