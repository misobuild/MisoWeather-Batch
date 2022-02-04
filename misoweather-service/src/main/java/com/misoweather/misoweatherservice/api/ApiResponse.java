package com.misoweather.misoweatherservice.api;

import com.misoweather.misoweatherservice.constants.HttpStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse {

    @Schema(example = "OK")
    private HttpStatusEnum status;

    @Schema(example = "행복하세요")
    private String message;
}