package com.couriertrackingservice.common.model;

import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    protected ApiResponse<Void> failure() {
        return new ApiResponse<>(false);
    }

    protected <T> ApiResponse<T> failure(T data) {
        return new ApiResponse<>(data,false);
    }
}