package com.misoweather.misoweatherservice.survey.repository;

import com.misoweather.misoweatherservice.config.JpaAuditingConfiguration;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.survey.Answer;
import com.misoweather.misoweatherservice.domain.survey.AnswerRepository;
import com.misoweather.misoweatherservice.domain.survey.Survey;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

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

    @Test
    @DisplayName("실패: <AnswerSurveyDto>의 answer.id에 해당하는 <Answer> 존재하지 않아 NOT_FOUND 반환한다.")
    void getAnswerFail(){
        // given
        AnswerSurveyDto givenAnswerSurveyDto = spy(AnswerSurveyDto.class);
        doReturn(9999L).when(givenAnswerSurveyDto).getAnswerId();

        // when, then
        assertThatThrownBy(() -> surveyService.getAnswer(givenAnswerSurveyDto))
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("성공: <AnswerSurveyDto>의 survey.id를 통해 <Survey>를 찾아 반환한다.")
    void getSurvey(){
        // given
        Survey givenSurvey = entityManager.find(Survey.class, 1L);
        AnswerSurveyDto givenAnswerSurveyDto = spy(AnswerSurveyDto.class);
        doReturn(givenSurvey.getId()).when(givenAnswerSurveyDto).getSurveyId();

        // when
        Survey actual = surveyService.getSurvey(givenAnswerSurveyDto);

        // then
        assertThat(actual.getId(), is(givenSurvey.getId()));
    }

    @Test
    @DisplayName("실패: <AnswerSurveyDto>의 survey.id에 해당하는 <AnsweSurveyr> 존재하지 않아 NOT_FOUND 반환한다.")
    void getSurveyFail(){
        // given
        AnswerSurveyDto givenAnswerSurveyDto = spy(AnswerSurveyDto.class);
        doReturn(9999L).when(givenAnswerSurveyDto).getSurveyId();

        // when, then
        assertThatThrownBy(() -> surveyService.getSurvey(givenAnswerSurveyDto))
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("실패: <Answer>의 소속 서베이와 주어진 <Survey>의 아이디가 다르면 에러 발생한다.")
    void checkAnswerAndSurveyFail(){
        // given
        Answer givenAnswer = entityManager.find(Answer.class, 1L);
        Survey givenSurvey = entityManager.find(Survey.class, 4L);

        // when, then
        assertThatThrownBy(() -> surveyService.checkAnswerAndSurvey(givenAnswer, givenSurvey))
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.CONFLICT.getMessage());
    }

    @Test
    @DisplayName("분기 테스트: <Answer>의 소속 서베이와 주어진 <Survey>의 아이디가 같으면 아무일도 일어나지 않는다.")
    void checkAnswerAndSurveyVoid(){
        // given
        Answer givenAnswer = entityManager.find(Answer.class, 1L);
        Survey givenSurvey = entityManager.find(Survey.class, 1L);

        // when, then
        assertDoesNotThrow(() -> surveyService.checkAnswerAndSurvey(givenAnswer, givenSurvey));
    }
}
