package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMapping;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMappingRepository;
import com.misoweather.misoweatherservice.domain.survey.Answer;
import com.misoweather.misoweatherservice.domain.survey.Survey;
import com.misoweather.misoweatherservice.global.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.mapping.service.MappingSurveyService;
import com.misoweather.misoweatherservice.survey.dto.AnswerStatusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@DisplayName("MappingService 테스트")
public class MappingSurveyServiceTest {

    @Mock
    private MemberSurveyMappingRepository memberSurveyMappingRepository;
    @InjectMocks
    private MappingSurveyService mappingSurveyService;

    @BeforeEach
    void setUp(){
        this.mappingSurveyService = new MappingSurveyService(memberSurveyMappingRepository);
    }

    @Test
    @DisplayName("분기 성공 테스트: <Member>로 조회하여 (Enum)RegionStatus로 필터해 (String)bigScale 가져온다.")
    void filterMemberSurveyMappingList(){
        // given
        Member givenMember = spy(Member.class);
        Survey givenSurvey = spy(Survey.class);

        MemberSurveyMapping givenMemberSurveyMapping = spy(MemberSurveyMapping.builder()
                .survey(givenSurvey)
                .member(givenMember)
                .answer(null)
                .shortBigScale(BigScaleEnum.getEnum("서울특별시").getShortValue())
                .build());

        given(memberSurveyMappingRepository.findByMemberAndSurvey(givenMember, givenSurvey)).willReturn(List.of(givenMemberSurveyMapping));
        doReturn(LocalDateTime.now()).when(givenMemberSurveyMapping).getCreatedAt();

        // when
        List<MemberSurveyMapping> memberRegionMappingList = mappingSurveyService.filterMemberSurveyMappingList(givenMember, givenSurvey);

        // then
        assertThat(memberRegionMappingList.get(0), is(givenMemberSurveyMapping));
    }

    @Test
    @DisplayName("분기 성공 테스트 - Wrong Year: member로 조회하여 RegionStatus로 필터해 bigScale 가져온다.")
    void filterMemberSurveyMappingListWhenWrongYear(){
        // given
        Member givenMember = spy(Member.class);
        Survey givenSurvey = spy(Survey.class);

        MemberSurveyMapping givenMemberSurveyMapping = spy(MemberSurveyMapping.builder()
                .survey(givenSurvey)
                .member(givenMember)
                .answer(null)
                .shortBigScale(BigScaleEnum.getEnum("서울특별시").getShortValue())
                .build());

        given(memberSurveyMappingRepository.findByMemberAndSurvey(givenMember, givenSurvey)).willReturn(List.of(givenMemberSurveyMapping));
        doReturn(LocalDateTime.now().minusYears(20)).when(givenMemberSurveyMapping).getCreatedAt();

        // when
        List<MemberSurveyMapping> memberRegionMappingWrongYearList = mappingSurveyService.filterMemberSurveyMappingList(givenMember, givenSurvey);

        // then
        assertThat(memberRegionMappingWrongYearList, is(List.of()));
    }

    @Test
    @DisplayName("분기 성공 테스트 - Wrong Month: member로 조회하여 RegionStatus로 필터해 bigScale 가져온다.")
    void filterMemberSurveyMappingListWhenWrongMonth(){
        // given
        Member givenMember = spy(Member.class);
        Survey givenSurvey = spy(Survey.class);

        MemberSurveyMapping givenMemberSurveyMapping = spy(MemberSurveyMapping.builder()
                .survey(givenSurvey)
                .member(givenMember)
                .answer(null)
                .shortBigScale(BigScaleEnum.getEnum("서울특별시").getShortValue())
                .build());

        given(memberSurveyMappingRepository.findByMemberAndSurvey(givenMember, givenSurvey)).willReturn(List.of(givenMemberSurveyMapping));
        doReturn(LocalDateTime.now().minusMonths(1)).when(givenMemberSurveyMapping).getCreatedAt();

        // when
        List<MemberSurveyMapping> memberRegionMappingWrongMonthList = mappingSurveyService.filterMemberSurveyMappingList(givenMember, givenSurvey);

        // then
        assertThat(memberRegionMappingWrongMonthList, is(List.of()));
    }

    @Test
    @DisplayName("분기 성공 테스트 - Wrong Days: member로 조회하여 RegionStatus로 필터해 bigScale 가져온다.")
    void filterMemberSurveyMappingListWhenWrongDays(){
        // given
        Member givenMember = spy(Member.class);
        Survey givenSurvey = spy(Survey.class);

        MemberSurveyMapping givenMemberSurveyMapping = spy(MemberSurveyMapping.builder()
                .survey(givenSurvey)
                .member(givenMember)
                .answer(null)
                .shortBigScale(BigScaleEnum.getEnum("서울특별시").getShortValue())
                .build());

        given(memberSurveyMappingRepository.findByMemberAndSurvey(givenMember, givenSurvey)).willReturn(List.of(givenMemberSurveyMapping));
        doReturn(LocalDateTime.now().minusDays(1)).when(givenMemberSurveyMapping).getCreatedAt();

        // when
        List<MemberSurveyMapping> memberRegionMappingWrongDaysList = mappingSurveyService.filterMemberSurveyMappingList(givenMember, givenSurvey);

        // then
        assertThat(memberRegionMappingWrongDaysList, is(List.of()));
    }

    @Test
    @DisplayName("분기 성공 테스트 - 존재하는 경우: 시간 조건에 맞는 <MemberSurveyMapping> 조회하여 존재하는지 확인한다.")
    void ifAnswerExistWhen(){
        // given
        Member givenMember = spy(Member.class);
        doReturn(9999L).when(givenMember).getMemberId();

        MemberSurveyMapping givenMemberSurveyMapping = spy(MemberSurveyMapping.builder()
                .survey(null)
                .member(givenMember)
                .answer(null)
                .shortBigScale(BigScaleEnum.getEnum("서울특별시").getShortValue())
                .build());

        given(memberSurveyMappingRepository.findByCreatedAtAfter(any(LocalDateTime.class))).willReturn(List.of(givenMemberSurveyMapping));

        // when
        Boolean result = mappingSurveyService.ifAnswerExist(givenMember);

        // then
        assertThat(result, is(Boolean.TRUE));
    }

    @Test
    @DisplayName("분기 성공 테스트 - 존재하지 않는 경우: 시간 조건에 맞는 <MemberSurveyMapping> 조회하여 존재하는지 확인한다.")
    void ifAnswerExistWhenNotFound(){
        // given
        Member givenMember = spy(Member.class);
        given(memberSurveyMappingRepository.findByCreatedAtAfter(any(LocalDateTime.class))).willReturn(List.of());

        // when
        Boolean result = mappingSurveyService.ifAnswerExist(givenMember);

        // then
        assertThat(result, is(Boolean.FALSE));
    }


    @Test
    @DisplayName("성공 테스트: <Member>, <Answer>, <Survey> 로 <MemberSurveyMapping> 빌드한다.")
    void buildFromFilteredMemberSurveyMappingList(){
        // when
        Member givenMember = spy(Member.class);
        Answer givenAnswer = spy(Answer.class);
        Survey givenSurvey = spy(Survey.class);
        List<Long> surveyIdList = new ArrayList<>(Arrays.asList(8888L, 9999L));
        MemberSurveyMapping givenMemberSurveyMapping = spy(MemberSurveyMapping.builder()
                .survey(givenSurvey)
                .member(givenMember)
                .answer(givenAnswer)
                .shortBigScale(BigScaleEnum.getEnum("서울특별시").getShortValue())
                .build());

        doReturn("안녕하세요").when(givenAnswer).getContent();
        doReturn(8888L).when(givenSurvey).getId();
        doReturn(LocalDateTime.now()).when(givenMemberSurveyMapping).getCreatedAt();

        given(memberSurveyMappingRepository.findByMember(givenMember)).willReturn(List.of(givenMemberSurveyMapping));

        // when
        List<AnswerStatusDto> answerStatusDtoList = mappingSurveyService.buildFromFilteredMemberSurveyMappingList(givenMember, surveyIdList);

        // then
        assertThat(answerStatusDtoList.get(0).getSurveyId(), is(8888L));
        assertThat(answerStatusDtoList.get(0).getMemberAnswer(), is("안녕하세요"));
        assertThat(answerStatusDtoList.get(0).getAnswered(), is(Boolean.TRUE));
        assertThat(surveyIdList, is(List.of(9999L)));
    }
}
