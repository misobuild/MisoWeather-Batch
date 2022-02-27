package com.misoweather.misoweatherservice.global.api;

import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponseWithData<T> extends ApiResponse{
    private final T data;

    @Builder
    public ApiResponseWithData(HttpStatusEnum status, String message, T data) {
        super(status, message);
        this.data = data;
    }
}
