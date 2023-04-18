package com.auiucloud.core.common.exception;

import com.auiucloud.core.common.api.IResultCode;
import lombok.Getter;

/**
 * @author dries
 * @date 2021/12/21
 */
@Getter
public class AuthException extends RuntimeException {

    private static final long serialVersionUID = 6346362155694433702L;

    private IResultCode resultCode;

    public AuthException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    protected AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
