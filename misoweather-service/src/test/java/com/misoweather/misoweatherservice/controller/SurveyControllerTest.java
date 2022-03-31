package com.misoweather.misoweatherservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.misoweather.misoweatherservice.config.SecurityConfig;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.global.api.ListDto;
import com.misoweather.misoweatherservice.global.exception.ControllerExceptionHandler;
import com.misoweather.misoweatherservice.mapping.reader.SurveyReader;
import com.misoweather.misoweatherservice.mapping.service.MappingSurveyService;
import com.misoweather.misoweatherservice.member.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.member.auth.UserDetailsImpl;
import com.misoweather.misoweatherservice.survey.dto.AnswerStatusDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyDtoBuilder;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyResponseDto;
import com.misoweather.misoweatherservice.survey.service.SimpleSurveyService;
import com.misoweather.misoweatherservice.survey.service.SurveyService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SurveyController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("MemberController 테스트")
public class SurveyControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private SurveyController surveyController;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SurveyService surveyService;
    @MockBean
    private MappingSurveyService mappingSurveyService;
    @MockBean
    private SimpleSurveyService simpleSurveyService;
    @MockBean
    private UserDetailsImpl userDetails;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeAll
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(surveyController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("성공: getSurveyStatus() 사용자의 서베이 답변 상태 가져오기")
    public void getSurveyStatus() throws Exception{
        // given
        Member givenMember = Member.builder().build();
        AnswerStatusDto givenAnswerStatusDto = AnswerStatusDto.builder().answered(Boolean.TRUE).build();

        given(userDetails.getMember()).willReturn(givenMember);
        given(simpleSurveyService.getAnswerStatus(any())).willReturn(ListDto.<AnswerStatusDto>builder()
                .responseList(List.of(givenAnswerStatusDto)).build());

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/survey/member")
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.responseList[0].answered").value(equalTo(Boolean.TRUE)))
                .andDo(print());
    }

    @Test
    @DisplayName("성공: getAnswer() 서베이 답변 목록 가져오기")
    public void getAnswer() throws Exception{
        // given
        AnswerStatusDto givenAnswerStatusDto = AnswerStatusDto.builder().surveyId(99999L).build();
        ListDto givenListDto = ListDto.builder().responseList(List.of(givenAnswerStatusDto)).build();

        given(surveyService.getAnswerList(anyLong())).willReturn(givenListDto);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/survey/answers/{surveyId}", 99999L)
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.responseList[0].surveyId").value(equalTo(99999)))
                .andDo(print());
    }

    @Test
    @DisplayName("성공: registerComment() 서베이 답변 입력")
    public void registerComment() throws Exception{
        // given
        Member givenMember = spy(Member.class);
        AnswerSurveyDto givenAnswerSurveyDto = AnswerSurveyDtoBuilder.build(99999L, 1111L, "경기");
        AnswerSurveyResponseDto givenAnswerSurveyResponseDto = AnswerSurveyResponseDto.builder().surveyDescription("Test Description").build();
        given(userDetails.getMember()).willReturn(givenMember);
        given(simpleSurveyService.answerSurvey(any(), any(AnswerSurveyDto.class))).willReturn(givenAnswerSurveyResponseDto);

        // when
        ResultActions result = this.mockMvc.perform(
                post("/api/survey")
                        .content(objectMapper.writeValueAsString(givenAnswerSurveyDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.surveyDescription").value("Test Description"))
                .andDo(print());
    }

    @Test
    @DisplayName("성공: getSurveyResult() 서베이 결과")
    public void getSurveyResult() throws Exception{
        // given
        SurveyReader givenSurveyReader = SurveyReader.builder().surveyId(9999L).surveyTitle("Test Survey Title").build();
        ListDto<SurveyReader> givenListDto = ListDto.<SurveyReader>builder().responseList(List.of(givenSurveyReader)).build();
        given(simpleSurveyService.getSurveyResultList(anyString())).willReturn(givenListDto);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/survey")
                        .param("shortBigScale", "경기")
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.responseList[0].surveyId").value(9999L))
                .andExpect(jsonPath("$.data.responseList[0].surveyTitle").value("Test Survey Title"))
                .andDo(print());
    }
}