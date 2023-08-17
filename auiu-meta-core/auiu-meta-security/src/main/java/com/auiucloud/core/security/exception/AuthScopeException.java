package com.auiucloud.core.security.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import java.io.Serial;

/**
 * @author dries
 * @description AuthScopeException 授权异常信息
 **/
public class AuthScopeException extends OAuth2AuthenticationException {

    @Serial
    private static final long serialVersionUID = -6883317284737740241L;


    public AuthScopeException(String errorCode) {
        super(errorCode);
    }

    public AuthScopeException(OAuth2Error error, Throwable cause) {
        super(error, cause);
    }
}
