package com.misoweather.misoweatherservice.domain.survey;

import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyJoinDto;

import java.util.List;

public interface AnswerRepositoryQueryDsl {
    List<AnswerSurveyJoinDto> findAnswerSurveyJoinBySurveyId(Long surveyId);
}
