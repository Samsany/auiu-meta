package com.auiucloud.core.security.processer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author dries
 * @date 2021/12/22
 */
@Slf4j
public abstract class AbstractValidateCodeProcessor implements ValidateCodeProcessor {

    @Override
    public void create(ServletWebRequest request) throws Exception {
        String validateCode = generate(request);
        save(request, validateCode);
        send(request, validateCode);
    }

    @Override
    public void validate(ServletWebRequest request) {
    }

    /**
     * 发送验证码，由子类实现
     *
     * @param request      请求
     * @param validateCode 验证码
     */
    protected abstract void send(ServletWebRequest request, String validateCode);

    /**
     * 保存验证码，保存到 redis 中
     *
     * @param request      请求
     * @param validateCode 验证码
     */
    private void save(ServletWebRequest request, String validateCode) {
    }

    /**
     * 生成验证码
     *
     * @param request 请求
     * @return 验证码
     */
    private String generate(ServletWebRequest request) {
        return null;
    }

}
