package com.misoweather.misoweatherservice.domain.survey;

import com.misoweather.misoweatherservice.dto.response.survey.AnswerSurveyJoinDto;

import java.util.List;

public interface AnswerRepositoryQueryDsl {
    List<AnswerSurveyJoinDto> findAnswerSurveyJoinBySurveyId(Long surveyId);
}
