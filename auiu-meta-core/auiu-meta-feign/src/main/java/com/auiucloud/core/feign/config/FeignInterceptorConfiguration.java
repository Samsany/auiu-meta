package com.auiucloud.core.feign.config;

/**
 * feign拦截器
 *
 * @author dries
 * @date 2021/12/22
 */
public class FeignInterceptorConfiguration {

    // /**
    //  * feign远程调用丢失请求头问题
    //  *
    //  * @return RequestInterceptor
    //  */
    // @Bean
    // public RequestInterceptor requestInterceptor() {
    //     return requestTemplate -> {
    //         //传递日志traceId
    //         String traceId = MDC.get(MetaConstant.LOG_TRACE_ID);
    //         if (StrUtil.isBlank(traceId)) {
    //             ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    //             if (attributes != null) {
    //                 // 1.使用RequestContextHolder获取在Controller层进入的请求的所有属性,底层是使用ThreadLocal的机制
    //                 HttpServletRequest request = attributes.getRequest();
    //                 // 2.同步请求头数据  (Cookie)
    //                 Enumeration<String> headerNames = request.getHeaderNames();
    //                 if (headerNames != null) {
    //                     String headerName;
    //                     while (headerNames.hasMoreElements()) {
    //                         headerName = headerNames.nextElement();
    //                         if (headerName.equalsIgnoreCase(MetaConstant.META_TRACE_ID)) {
    //                             traceId = request.getHeader(headerName);
    //                             requestTemplate.header(MetaConstant.META_TRACE_ID, traceId);
    //                             TraceUtil.mdcTraceId(traceId);
    //                         }
    //                         String values = request.getHeader(headerName);
    //                         requestTemplate.header(headerName, values);
    //                     }
    //                 }
    //             }
    //         } else {
    //             requestTemplate.header(MetaConstant.META_TRACE_ID, traceId);
    //         }
    //     };
    // }

}
