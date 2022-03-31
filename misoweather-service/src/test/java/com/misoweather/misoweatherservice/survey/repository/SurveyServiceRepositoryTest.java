package com.misoweather.misoweatherservice.survey.repository;

import com.misoweather.misoweatherservice.config.JpaAuditingConfiguration;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.survey.Answer;
import com.misoweather.misoweatherservice.domain.survey.AnswerRepository;
import com.misoweather.misoweatherservice.domain.survey.SurveyRepository;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyDto;
import com.misoweather.misoweatherservice.survey.service.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfiguration.class)
@DisplayName("SurveyService에서 비즈니스 로직 없는 JPA 활용 부분을 테스트한다. 테스트")
public class SurveyServiceRepositoryTest {
    private SurveyService surveyService;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        this.surveyService = new SurveyService(surveyRepository, answerRepository);
    }

    @Test
    @DisplayName("성공: <AnswerSurveyDto>의 answer.id를 통해 <Answer>를 찾아 반환한다.")
    void getAnswer(){
        // given
        Answer givenAnswer = entityManager.find(Answer.class, 1L);
        AnswerSurveyDto givenAnswerSurveyDto = spy(AnswerSurveyDto.class);
        doReturn(givenAnswer.getId()).when(givenAnswerSurveyDto).getAnswerId();

        // when
        Answer actual = surveyService.getAnswer(givenAnswerSurveyDto);

        // then
        assertThat(actual.getId(), is(givenAnswer.getId()));
    }
}
