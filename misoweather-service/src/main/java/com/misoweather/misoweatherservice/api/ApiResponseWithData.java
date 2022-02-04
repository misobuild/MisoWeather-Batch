package com.misoweather.misoweatherservice.api;

import com.misoweather.misoweatherservice.constants.HttpStatusEnum;
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
