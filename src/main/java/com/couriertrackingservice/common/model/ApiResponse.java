package com.couriertrackingservice.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    @Setter
    private Boolean success;

    private T data;

    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
    }

    public ApiResponse(boolean success) {
        this.setSuccess(success);
    }

    public ApiResponse(T data, boolean success) {
        this.data = data;
        this.setSuccess(success);
    }
}