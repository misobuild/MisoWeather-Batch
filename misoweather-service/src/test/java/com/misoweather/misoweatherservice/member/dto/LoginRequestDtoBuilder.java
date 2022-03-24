package com.misoweather.misoweatherservice.member.dto;

public class LoginRequestDtoBuilder {
    public static LoginRequestDto build(String socialId, String socialType) {
        return new LoginRequestDto(socialId, socialType);
    }
}
