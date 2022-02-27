package com.misoweather.misoweatherservice.domain.survey;

import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyJoinDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.misoweather.misoweatherservice.domain.survey.QAnswer.answer;
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
                        answer.id, answer.description, answer.answer
                        , survey.id, survey.description, survey.title))
                .from(answer)
                .join(answer.survey, survey)
                .where(answer.survey.id.eq(surveyId))
                .fetch();
    }

}