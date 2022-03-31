package com.misoweather.misoweatherservice.survey.service;

import com.misoweather.misoweatherservice.domain.survey.AnswerRepository;
import com.misoweather.misoweatherservice.domain.survey.Survey;
import com.misoweather.misoweatherservice.domain.survey.SurveyRepository;
import com.misoweather.misoweatherservice.global.api.ListDto;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyJoinDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyJoinDtoBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService 테스트")
public class SurveyServiceTest {

    @Mock private SurveyRepository surveyRepository;
    @Mock private AnswerRepository answerRepository;
    @InjectMocks private SurveyService surveyService;

    @BeforeEach
    void setUp(){
        this.surveyService = new SurveyService(surveyRepository, answerRepository);
    }

    @Test
    @DisplayName("성공: getAnswerList() <Long>surveyId를 통해 List<AnswerSurveyJoinDto> 찾아 반환한다.")
    void getAnswerList(){
        // given
        Long givenSurveyId = 99999L;
        AnswerSurveyJoinDto givenAnswerSurveyJoinDto = AnswerSurveyJoinDtoBuilder
                .build(9999L, "test answer dsecrpiton", "test answer", givenSurveyId, "test survey description", "test survey title");
        given(answerRepository.findAnswerSurveyJoinBySurveyId(anyLong())).willReturn(List.of(givenAnswerSurveyJoinDto));

        // when
        ListDto<AnswerSurveyJoinDto> actual = surveyService.getAnswerList(givenSurveyId);

        // then
        assertThat(actual.getResponseList().get(0).getSurveyId(), is(givenSurveyId));
    }

    @Test
    @DisplayName("분기 테스트: getAnswerList() survey 존재하지 않으면 에러 발생시킨다.")
    void getAnswerListWhenListNull(){
        // given
        Long givenSurveyId = 99999L;
        given(answerRepository.findAnswerSurveyJoinBySurveyId(anyLong())).willReturn(List.of());

        // when, then
        assertThatThrownBy(() -> surveyService.getAnswerList(givenSurveyId))
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.NOT_FOUND.getMessage());
    }
}
