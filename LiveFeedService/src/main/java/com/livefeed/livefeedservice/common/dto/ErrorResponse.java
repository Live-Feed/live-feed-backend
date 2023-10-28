package com.livefeed.livefeedservice.common.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        boolean success,
        int status,
        String message
) {
    public static ErrorResponse badRequest(String message) {
        return new ErrorResponse(false, HttpStatus.BAD_REQUEST.value(), message);
    }
}
