package com.livefeed.livefeedservice.common.controller;

import com.livefeed.livefeedservice.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ErrorResponse TypeMismatchException(IllegalArgumentException e) {
        log.error("IllegalArgumentException ", e);
        return ErrorResponse.badRequest(e.getMessage());
    }
}
