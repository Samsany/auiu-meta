package com.auiucloud.component.config;

import com.auiucloud.component.service.ISysConfigService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class OSSConfigCache {

    private final ISysConfigService sysConfigService;

    @PostConstruct
    public void init() {
        sysConfigService.clearOss();
        //加载OSS配置文件
        sysConfigService.getOssProperties();
    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
        sysConfigService.clearOss();
    }

    @Scheduled(cron = "0 0 0/2 * * ?")
    public void taskInit() {
        //每2小时执行一次缓存
        init();
    }

}
