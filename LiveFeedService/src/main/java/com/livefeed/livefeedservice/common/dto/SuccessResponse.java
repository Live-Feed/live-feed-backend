package com.livefeed.livefeedservice.common.dto;

public record SuccessResponse<T>(
        boolean success,
        int status,
        String message,
        T data
) {
    public static <T> SuccessResponse<T> ok(String message, T data) {
        return new SuccessResponse<>(
                true, 200, message, data
        );
    }
}
