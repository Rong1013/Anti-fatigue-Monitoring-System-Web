package com.fatigue.monitoring.exception;

import com.fatigue.monitoring.common.ResultCode;
import lombok.Getter;

/**
 * 自定义业务异常
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public BusinessException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
}
