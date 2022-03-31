package com.misoweather.misoweatherservice.survey.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    AnswerSurveyDto(Long surveyId, Long answerId, String shortBigScale) {
        this.surveyId = surveyId;
        this.answerId = answerId;
        this.shortBigScale = shortBigScale;
    }
}

