package com.misoweather.misoweatherservice.survey.dto;

public class AnswerSurveyJoinDtoBuilder {
    public static AnswerSurveyJoinDto build(Long answerId, String answerDescription, String answer, Long surveyId, String surveyDescription, String surveyTitle) {
        return new AnswerSurveyJoinDto(answerId, answerDescription, answer, surveyId, surveyDescription, surveyTitle);
    }
}
