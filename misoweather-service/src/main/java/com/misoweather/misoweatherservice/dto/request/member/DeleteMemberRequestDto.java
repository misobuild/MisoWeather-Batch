package com.misoweather.misoweatherservice.dto.request.member;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteMemberRequestDto {
    @NotNull
    @Schema(example = "2063494098")
    private String socialId;

    @NotNull
    @Schema(example = "kakao")
    private String socialType;
}
