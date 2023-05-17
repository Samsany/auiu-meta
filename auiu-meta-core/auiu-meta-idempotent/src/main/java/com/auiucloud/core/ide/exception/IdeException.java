package com.auiucloud.core.ide.exception;

import java.io.Serial;

/**
 * @author dries
 **/
public class IdeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -851115183208290929L;

    public IdeException(String message) {
        super(message);
    }
}
