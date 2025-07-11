package com.cauamattosprj.limebank.domains.common.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Instant timestamp = Instant.now();
    private boolean success = true;
    private String message = null;
    private T data = null;
    private int status;
    private String path = null;

    @Builder(access = AccessLevel.PRIVATE)
    private ApiResponse(Instant timestamp, boolean success, String message, T data, int status, String path) {
        this.timestamp = timestamp;
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status;
        this.path = path;
    }

    public static <T> ApiResponse<T> ofSuccess(T data, int status) {
        return ApiResponse.<T>builder()
                .data(data)
                .status(status)
                .success(true)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> ofSuccess(int status) {
        return ApiResponse.<T>builder()
                .status(status)
                .success(true)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> ofSuccess(T data, int status, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .status(status)
                .message(message)
                .success(true)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> ofError(T data, int status) {
        return ApiResponse.<T>builder()
                .data(data)
                .status(status)
                .success(false)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> ofError(String message, int status) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(status)
                .success(false)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> ofError(T data, int status, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .status(status)
                .message(message)
                .success(false)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> ofDebug(T data, int status, String path) {
        return ApiResponse.<T>builder()
                .data(data)
                .status(status)
                .success(false)
                .timestamp(Instant.now())
                .path(path)
                .build();
    }
}


