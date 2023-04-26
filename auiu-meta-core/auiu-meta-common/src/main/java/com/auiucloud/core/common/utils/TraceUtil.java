package com.auiucloud.core.common.utils;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.constant.MetaConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

/**
 * 链路追踪工具类
 *
 * @author dries
 * @date 2021/12/20
 */
public class TraceUtil {

    /**
     * 从header和参数中获取traceId
     * 从前端传入数据
     *
     * @param request　HttpServletRequest
     * @return traceId
     */
    public static String getTraceId(HttpServletRequest request) {
        String traceId = request.getParameter(MetaConstant.META_TRACE_ID);
        if (StrUtil.isBlank(traceId)) {
            traceId = request.getHeader(MetaConstant.META_TRACE_ID);
        }
        return traceId;
    }

    /**
     * 传递traceId至MDC
     * @param traceId　跟踪ID
     */
    public static void mdcTraceId (String traceId) {
        if (StrUtil.isNotBlank(traceId)) {
            MDC.put(MetaConstant.LOG_TRACE_ID, traceId);
        }
    }

}
