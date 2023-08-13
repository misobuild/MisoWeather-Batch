package com.misoweather.misoweatherservice.member.dto;

public class SingUpRequestDtoBuilder {
    public static SignUpRequestDto build(String socialId, String socialType, String nickname, String emoji, Long defaultRegionId) {
        return new SignUpRequestDto(socialId, socialType, nickname, emoji, defaultRegionId);
    }
}
