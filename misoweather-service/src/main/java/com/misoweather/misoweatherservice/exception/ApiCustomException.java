package com.misoweather.misoweatherservice.exception;

import com.misoweather.misoweatherservice.constants.HttpStatusEnum;
import lombok.Getter;

@Getter
public class ApiCustomException extends RuntimeException {

    private final HttpStatusEnum httpStatusEnum;

    public ApiCustomException(HttpStatusEnum httpStatusEnum){
        super(httpStatusEnum.getMessage());
        this.httpStatusEnum = httpStatusEnum;
    }
}