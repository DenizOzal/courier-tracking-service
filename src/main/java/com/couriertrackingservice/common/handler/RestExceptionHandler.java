package com.couriertrackingservice.common.handler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.couriertrackingservice.common.exception.SystemException;
import com.couriertrackingservice.common.model.ApiResponse;
import com.couriertrackingservice.common.model.BaseController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class RestExceptionHandler extends BaseController {

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SystemException.class)
    public ApiResponse<String> handleSystemException(SystemException ex) {
        return failure(ex.getMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> exception(Exception ex) {
        log.error("Error occurred in " +  ex.getMessage());
        return failure();
    }
}