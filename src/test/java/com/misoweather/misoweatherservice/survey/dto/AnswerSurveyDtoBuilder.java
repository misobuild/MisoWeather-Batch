package com.misoweather.misoweatherservice.survey.dto;

public class AnswerSurveyDtoBuilder {
    public static AnswerSurveyDto build(Long surveyId, Long answerId, String shortBigScale){
        return new AnswerSurveyDto(surveyId, answerId, shortBigScale);
    }
}
