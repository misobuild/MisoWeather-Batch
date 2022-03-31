package com.misoweather.misoweatherservice.survey.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerSurveyJoinDto {
    private Long answerId;
    private String answerDescription;
    private String answer;
    private Long surveyId;
    private String surveyDescription;
    private String surveyTitle;

    AnswerSurveyJoinDto(Long answerId, String answerDescription, String answer, Long surveyId, String surveyDescription, String surveyTitle) {
        this.answerId = answerId;
        this.answerDescription = answerDescription;
        this.answer = answer;
        this.surveyId = surveyId;
        this.surveyDescription = surveyDescription;
        this.surveyTitle = surveyTitle;
    }
}
