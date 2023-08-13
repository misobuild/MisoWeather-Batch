package com.misoweather.misoweatherservice.member.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {
    @NotNull
    @Schema(example = "2063494098")
    private String socialId;

    @NotNull
    @Schema(example = "kakao")
    private String socialType;

    LoginRequestDto(String socialId, String socialType) {
        this.socialId = socialId;
        this.socialType = socialType;
    }
}
