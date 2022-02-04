package com.misoweather.misoweatherservice.utils.builder;

import lombok.Getter;

@Getter
public abstract class ApiUrlBuilder {
    public String urlLink;
    String dataType = "JSON";
}
