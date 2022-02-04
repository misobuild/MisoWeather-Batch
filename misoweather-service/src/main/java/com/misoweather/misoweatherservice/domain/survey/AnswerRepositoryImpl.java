package com.misoweather.misoweatherservice.domain.survey;

import com.misoweather.misoweatherservice.dto.response.survey.AnswerSurveyJoinDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.misoweather.misoweatherservice.domain.survey.QAnswer.answer1;
import static com.misoweather.misoweatherservice.domain.survey.QSurvey.survey;

public class AnswerRepositoryImpl implements AnswerRepositoryQueryDsl {

    private final JPAQueryFactory queryFactory;

    public AnswerRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<AnswerSurveyJoinDto> findAnswerSurveyJoinBySurveyId(Long surveyId) {
        return queryFactory
                .select(Projections.constructor(AnswerSurveyJoinDto.class,
                        answer1.id, answer1.description, answer1.answer
                , survey.id, survey.description, survey.title))
                .from(answer1)
                .join(answer1.survey, survey)
                .where(answer1.survey.id.eq(surveyId))
                .fetch();
    }
}