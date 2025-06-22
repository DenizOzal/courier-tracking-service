package com.couriertrackingservice.common.exception;

import com.couriertrackingservice.common.model.ApiError;
import com.couriertrackingservice.common.model.ErrorType;

import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {

    private final String code;

    private final ErrorType type;

    public SystemException(ApiError error) {
        super(error.getMessage());
        this.code = error.getCode();
        this.type = error.getErrorType();
    }
}