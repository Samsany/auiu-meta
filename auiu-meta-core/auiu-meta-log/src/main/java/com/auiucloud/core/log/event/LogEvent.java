package com.auiucloud.core.log.event;

import org.springframework.context.ApplicationEvent;

/**
 * 日志事件
 *
 * @author dries
 * @date 2021/12/24
 */
public class LogEvent extends ApplicationEvent {
    private static final long serialVersionUID = 878890078639401091L;

    public LogEvent(Object source) {
        super(source);
    }

}
