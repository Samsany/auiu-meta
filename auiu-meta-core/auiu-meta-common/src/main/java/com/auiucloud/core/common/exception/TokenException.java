package com.auiucloud.core.common.exception;

/**
 * Token处理异常
 *
 * @author dries
 * @date 2021/12/22
 */
public class TokenException extends RuntimeException {

    private static final long serialVersionUID = -109638013567525177L;

    public TokenException(String message) {
        super(message);
    }

}
