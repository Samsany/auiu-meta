package com.auiucloud.core.common.exception;

import com.auiucloud.core.common.api.IResultCode;
import lombok.Getter;

import java.io.Serial;

/**
 * 自定义异常
 *
 * @author dries
 * @date 2021/12/21
 */
@Getter
public class ApiException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2606114516331613442L;

    private IResultCode resultCode;

    public ApiException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
