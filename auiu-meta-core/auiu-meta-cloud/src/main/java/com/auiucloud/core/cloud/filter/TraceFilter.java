package com.auiucloud.core.cloud.filter;

import com.auiucloud.core.cloud.props.MetaRequestProperties;
import com.auiucloud.core.common.utils.TraceUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 日志链路追踪过滤器
 *
 * @author dries
 * @date 2021/12/20
 */
@ConditionalOnClass(Filter.class)
public class TraceFilter extends OncePerRequestFilter {

    @Autowired
    private MetaRequestProperties metaRequestProperties;

    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request) throws ServletException {
        return !metaRequestProperties.isTrace();
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId = TraceUtil.getTraceId(request);
            TraceUtil.mdcTraceId(traceId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

}
