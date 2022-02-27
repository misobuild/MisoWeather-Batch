package com.misoweather.misoweatherservice.member.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    @NotNull
    @Schema(example = "2063494098")
    private String socialId;

    @NotNull
    @Schema(example = "kakao")
    private String socialType;

    @NotNull
    @Schema(example = "희망찬 아기기름램프")
    private String nickname;

    @NotNull
    @Schema(example = "\uD83E\uDE94")
    private String emoji;

    @NotNull
    @Schema(example = "1241")
    private Long defaultRegionId;
}
