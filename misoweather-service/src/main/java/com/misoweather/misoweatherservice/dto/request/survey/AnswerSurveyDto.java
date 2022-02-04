package com.misoweather.misoweatherservice.dto.request.survey;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerSurveyDto {
    @NotNull
    @Schema(example = "1")
    private Long surveyId;
    @NotNull
    @Schema(example = "1")
    private Long answerId;
    @NotNull
    @Schema(example = "세종")
    private String shortBigScale;
}

