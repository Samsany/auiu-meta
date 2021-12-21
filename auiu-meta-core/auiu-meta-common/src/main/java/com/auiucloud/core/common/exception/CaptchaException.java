package com.auiucloud.core.common.exception;

import com.auiucloud.core.common.api.IResultCode;
import lombok.Getter;

/**
 * @author dries
 * @date 2021/12/21
 */
@Getter
public class CaptchaException extends Exception {
    private static final long serialVersionUID = 6346362155694433702L;

    private IResultCode resultCode;

    public CaptchaException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public CaptchaException(String message) {
        super(message);
    }

    public CaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaException(Throwable cause) {
        super(cause);
    }

    protected CaptchaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
