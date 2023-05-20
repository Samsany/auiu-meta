package com.auiucloud.core.log.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.auiucloud.core.common.constant.MetaConstant;
import com.auiucloud.core.common.model.CommonLog;
import com.auiucloud.core.common.model.IpAddress;
import com.auiucloud.core.common.utils.*;
import com.auiucloud.core.common.utils.http.RequestHolder;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.log.event.LogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 日志拦截器
 *
 * @author dries
 * @date 2021/12/24
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private final ApplicationContext applicationContext;

    /**
     * 获取方法参数信息
     *
     * @param method         方法
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     */
    public static MethodParameter getMethodParameter(Method method, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(method, parameterIndex);
        methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
        return methodParameter;
    }

    @Pointcut("@annotation(com.auiucloud.core.log.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object recordLog(ProceedingJoinPoint point) throws Throwable {

        Object result = new Object();
        //　获取request
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        // 判断为空则直接跳过执行
        if (ObjectUtils.isEmpty(request)) {
            return point.proceed();
        }

        //　获取注解里的value值
        Method targetMethod = resolveMethod(point);
        Log annotation = targetMethod.getAnnotation(Log.class);
        // 执行时长(毫秒)
        long beginTime = System.nanoTime();
        // 请求方法
        String method = request.getMethod();
        String url = request.getRequestURI();
        // 获取IP和地区
        String ip = IPUtil.getHttpServletRequestIpAddress();
        // IpAddress ipAddress = IPUtil.getIpAddress(ip);
        // String region = StrUtil.isBlank(ipAddress.getRegion()) ? "本地" : ipAddress.getRegion();

        // 参数
        Object[] args = point.getArgs();
        String requestParam = getArgs(args, request);

        // 计算耗时
        long tookTime = 0L;
        try {
            result = point.proceed();
        } finally {
            tookTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - beginTime);
        }
        //　如果是登录请求，则不获取用户信息
        String username = null;
        if (!url.contains("oauth")) {
            // 判断header是否存在，存在则获取用户名
            try {
                username = SecurityUtil.getUsername();
            } catch (Exception ignored) {
            }
        }
        //　组装CommonLog
        CommonLog commonLog = CommonLog.builder()
                .ip(ip)
                .createBy(username)
                .method(method)
                .url(url)
                .operation(String.valueOf(result))
                // .location(region)
                .traceId(request.getHeader(MetaConstant.LOG_TRACE_ID))
                .executeTime(tookTime)
                .title(annotation.value())
                .params(JSONUtil.toJsonStr(requestParam))
                .build();
        // log.info("Http Request: {}", JSONObject.toJSONString(commonLog));
        // 发布事件
        applicationContext.publishEvent(new LogEvent(commonLog));
        return result;
    }

    private Method resolveMethod(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();

        Method method = getDeclaredMethod(targetClass, signature.getName(),
                signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("无法解析目标方法: " + signature.getMethod().getName());
        }
        return method;
    }

    private Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethod(superClass, name, parameterTypes);
            }
        }
        return null;
    }

    /**
     * 获取请求参数
     *
     * @param args
     * @param request
     * @return
     */
    private String getArgs(Object[] args, HttpServletRequest request) {
        String strArgs = StringPool.EMPTY;

        try {
            if (!request.getContentType().contains("multipart/form-data")) {
                strArgs = JSONObject.toJSONString(args);
            }
        } catch (Exception e) {
            try {
                strArgs = Arrays.toString(args);
            } catch (Exception ex) {
                log.warn("解析参数异常", ex);
            }
        }
        return strArgs;
    }

    /**
     * 配置异常通知
     *
     * @param point join point for advice
     * @param e     exception
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void logAfterThrowing(JoinPoint point, Throwable e) {
        // 打印执行时间
        long startTime = System.nanoTime();

        // 获取IP和地区
        String ip = IPUtil.getHttpServletRequestIpAddress();
        IpAddress ipAddress = IPUtil.getIpAddress(ip);
        // String region = StrUtil.isBlank(ipAddress.getRegion()) ? "本地" : ipAddress.getRegion();


        //　获取request
        HttpServletRequest request = RequestHolder.getHttpServletRequest();

        // 请求方法
        String method = request.getMethod();
        String url = request.getRequestURI();

        //　获取注解里的value值
        Method targetMethod = resolveMethod((ProceedingJoinPoint) point);
        Log logAnn = targetMethod.getAnnotation(Log.class);

        //　组装CommonLog
        CommonLog commonLog = CommonLog.builder()
                .ip(ip)
                .method(method)
                .url(url)
                // .location(region)
                .traceId(TraceUtil.getTraceId(request))
                .executeTime(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime))
                .title(logAnn.value())
                .type("2")
                .exception(ThrowableUtil.getStackTrace(e))
                .build();
        // 设置MDC
        TraceUtil.mdcTraceId(TraceUtil.getTraceId(request));
        // 发布事件
        applicationContext.publishEvent(new LogEvent(commonLog));
        log.info("Error Result: {}", commonLog);
    }

}
