package com.misoweather.misoweatherservice.global.exception;

import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import lombok.Getter;

@Getter
public class ApiCustomException extends RuntimeException {

    private final HttpStatusEnum httpStatusEnum;

    public ApiCustomException(HttpStatusEnum httpStatusEnum){
        super(httpStatusEnum.getMessage());
        this.httpStatusEnum = httpStatusEnum;
    }
}