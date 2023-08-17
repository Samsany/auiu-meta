package com.auiucloud.core.security.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import java.io.Serial;

/**
 * @author dries
 **/
public class OAuthClientException extends OAuth2AuthenticationException {

    @Serial
    private static final long serialVersionUID = -7256672857055438785L;

    public OAuthClientException(String errorCode) {
        super(errorCode);
    }

    public OAuthClientException(OAuth2Error error, Throwable cause) {
        super(error, cause);
    }

}
