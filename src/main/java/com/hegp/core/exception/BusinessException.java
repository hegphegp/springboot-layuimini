package com.hegp.core.exception;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private int httpCode = 400;

    public BusinessException(int httpCode, String message) {
        super(message);
        this.httpCode = httpCode;
    }

    public BusinessException(int httpCode, String message, Throwable cause) {
        super(message, cause);
        this.httpCode = httpCode;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

}
