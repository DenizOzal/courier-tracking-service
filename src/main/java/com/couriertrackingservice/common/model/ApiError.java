package com.couriertrackingservice.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiError {

    INTERNAL_ERROR("1", "internal error is occurred", ErrorType.INTERNAL);

    private final String code;
    private final String message;
    private final ErrorType errorType;
}