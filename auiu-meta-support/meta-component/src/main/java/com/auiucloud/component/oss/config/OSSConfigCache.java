package com.auiucloud.component.oss.config;

import com.auiucloud.component.oss.service.IOSSConfigService;
import com.auiucloud.component.sysconfig.service.ISysConfigService;
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
public class OSSConfigCache {

    private final IOSSConfigService ossConfigService;

    @PostConstruct
    public void init() {
        ossConfigService.clearOss();
        // 加载OSS配置文件
        ossConfigService.getOssProperties();
    }

    @PreDestroy
    public void destroy() {
        // 系统运行结束
        ossConfigService.clearOss();
    }

    @Scheduled(cron = "0 0 0/2 * * ?")
    public void taskInit() {
        // 每2小时执行一次缓存
        init();
    }
}
