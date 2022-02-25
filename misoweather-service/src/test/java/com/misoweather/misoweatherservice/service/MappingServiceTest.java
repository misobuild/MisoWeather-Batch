package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.constants.RegionEnum;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMapping;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMappingRepository;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.domain.survey.Answer;
import com.misoweather.misoweatherservice.domain.survey.Survey;
import com.misoweather.misoweatherservice.dto.response.survey.AnswerStatusDto;
import com.misoweather.misoweatherservice.exception.ApiCustomException;
import org.junit.jupiter.api.Assertions;
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
public class MappingServiceTest {

    @Mock
    private MemberSurveyMappingRepository memberSurveyMappingRepository;
    @Mock
    private MemberRegionMappingRepository memberRegionMappingRepository;
    @InjectMocks
    private MappingService mappingService;

    @BeforeEach
    void setUp(){
        this.mappingService = new MappingService(memberRegionMappingRepository, memberSurveyMappingRepository);
    }

    @Test
    @DisplayName("member로 조회하여 RegionStatus로 필터해 bigScale 가져온다.")
    void filterMemberSurveyMappingList(){
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

        List<MemberSurveyMapping> memberRegionMappingList = mappingService.filterMemberSurveyMappingList(givenMember, givenSurvey);
        assertThat(memberRegionMappingList.get(0), is(givenMemberSurveyMapping));

        doReturn(LocalDateTime.now().minusYears(20)).when(givenMemberSurveyMapping).getCreatedAt();
        List<MemberSurveyMapping> memberRegionMappingWrongYearList = mappingService.filterMemberSurveyMappingList(givenMember, givenSurvey);
        assertThat(memberRegionMappingWrongYearList, is(List.of()));

        doReturn(LocalDateTime.now().minusMonths(1)).when(givenMemberSurveyMapping).getCreatedAt();
        List<MemberSurveyMapping> memberRegionMappingWrongMonthList = mappingService.filterMemberSurveyMappingList(givenMember, givenSurvey);
        assertThat(memberRegionMappingWrongMonthList, is(List.of()));

        doReturn(LocalDateTime.now().minusDays(1)).when(givenMemberSurveyMapping).getCreatedAt();
        List<MemberSurveyMapping> memberRegionMappingWrongDaysList = mappingService.filterMemberSurveyMappingList(givenMember, givenSurvey);
        assertThat(memberRegionMappingWrongDaysList, is(List.of()));
    }

    @Test
    @DisplayName("MappingService: ifAnswerExist()")
    void ifAnswerExistWhen(){
        Member givenMember = spy(Member.class);
        doReturn(9999L).when(givenMember).getMemberId();

        MemberSurveyMapping givenMemberSurveyMapping = spy(MemberSurveyMapping.builder()
                .survey(null)
                .member(givenMember)
                .answer(null)
                .shortBigScale(BigScaleEnum.getEnum("서울특별시").getShortValue())
                .build());

        given(memberSurveyMappingRepository.findByCreatedAtAfter(any(LocalDateTime.class))).willReturn(List.of(givenMemberSurveyMapping));
        assertThat(mappingService.ifAnswerExist(givenMember), is(Boolean.TRUE));
    }

    @Test
    @DisplayName("MappingService: ifAnswerExist() when List Not Found")
    void ifAnswerExistWhenNotFound(){
        Member givenMember = spy(Member.class);
        given(memberSurveyMappingRepository.findByCreatedAtAfter(any(LocalDateTime.class))).willReturn(List.of());

        Boolean result = mappingService.ifAnswerExist(givenMember);

        assertThat(result, is(Boolean.FALSE));
    }


    @Test
    @DisplayName("MappingService: buildFromFilteredMemberSurveyMappingList 테스트")
    void buildFromFilteredMemberSurveyMappingList(){
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

        List<AnswerStatusDto> answerStatusDtoList = mappingService.buildFromFilteredMemberSurveyMappingList(givenMember, surveyIdList);

        assertThat(answerStatusDtoList.get(0).getSurveyId(), is(8888L));
        assertThat(answerStatusDtoList.get(0).getMemberAnswer(), is("안녕하세요"));
        assertThat(answerStatusDtoList.get(0).getAnswered(), is(Boolean.TRUE));
        assertThat(surveyIdList, is(List.of(9999L)));
    }


    @Test
    @DisplayName("filterMemberRegionMappingList(): 현재 날짜와 같은 mapping만 남도록 filter 한다.")
    void filterMemberRegionMappingList() {
        Member givenMember = Member.builder()
                .socialId("12345")
                .emoji("a")
                .nickname("행복한 가짜광대")
                .socialType("kakao")
                .build();

        Region givenRegion = spy(Region.class);

        MemberRegionMapping memberRegionMapping = MemberRegionMapping.builder()
                .region(givenRegion)
                .member(givenMember)
                .regionStatus(RegionEnum.DEFAULT)
                .build();

        List<MemberRegionMapping> memberRegionMappingList = List.of(memberRegionMapping);
        List<MemberRegionMapping> memberRegionMappingEmptyList = List.of();

        // when
        MemberRegionMapping filteredMemberRegionMapping = mappingService.filterMemberRegionMappingList(memberRegionMappingList);
        ApiCustomException exceptionThrown = Assertions.assertThrows(
                ApiCustomException.class,
                () -> {
                    mappingService.filterMemberRegionMappingList(memberRegionMappingEmptyList);
                }
        );

        assertThat(filteredMemberRegionMapping, is(memberRegionMappingList.get(0)));
        assertThat(exceptionThrown.getMessage(), is("NOT_FOUND"));
    }
}
