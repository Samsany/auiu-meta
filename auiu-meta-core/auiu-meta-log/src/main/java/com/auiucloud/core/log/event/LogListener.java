package com.auiucloud.core.log.event;

import com.auiucloud.core.common.model.CommonLog;
import com.auiucloud.core.log.feign.ICommonLogService;
import com.auiucloud.core.log.feign.ISysLogService;
import com.auiucloud.core.log.props.LogProperties;
import com.auiucloud.core.log.props.LogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author dries
 * @date 2021/12/24
 */
@Slf4j
@Component
public class LogListener {

    private ISysLogService sysLogService;
    private ICommonLogService commonLogService;
    private LogProperties logProperties;

    public LogListener() {

    }

    public LogListener(ISysLogService sysLogService, LogProperties logProperties) {
        this.sysLogService = sysLogService;
        this.logProperties = logProperties;
    }

    public LogListener(ICommonLogService commonLogService, LogProperties logProperties) {
        this.commonLogService = commonLogService;
        this.logProperties = logProperties;
    }

    @Async
    @Order
    @EventListener(LogEvent.class)
    public void saveSysLog(LogEvent event) {
        CommonLog commonLog = (CommonLog) event.getSource();
        // 发送日志到kafka
        log.info("发送日志:{}", commonLog);
        if (logProperties.getLogType().equals(LogType.KAFKA)) {
            commonLogService.sendCommonLog(commonLog);
        } else {
            sysLogService.set(commonLog);
        }
    }

}
