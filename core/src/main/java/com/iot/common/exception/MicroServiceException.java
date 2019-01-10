package com.iot.common.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class MicroServiceException extends HystrixBadRequestException {

    public MicroServiceException(String message) {
        super(message);
    }

    public MicroServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
