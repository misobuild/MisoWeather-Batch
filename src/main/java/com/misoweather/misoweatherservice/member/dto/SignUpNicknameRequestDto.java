package com.misoweather.misoweatherservice.member.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpNicknameRequestDto {
    @NotNull
    private String nickname;
}