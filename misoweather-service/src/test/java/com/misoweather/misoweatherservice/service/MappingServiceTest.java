package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMapping;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMappingRepository;
import com.misoweather.misoweatherservice.domain.survey.Survey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;


//TODO spy를 사용하여 auditing으로 mock객체 생성으로만 해결할 수 없는 부분을 해결했다.는 내용을 기록하자
@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService 테스트")
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
        // given
        Member givenMember = spy(Member.class);
        Survey givenSurvey = spy(Survey.class);

        MemberSurveyMapping givenMemberSurveyMapping = spy(MemberSurveyMapping.builder()
                .survey(givenSurvey)
                .member(givenMember)
                .answer(null)
                .shortBigScale(BigScaleEnum.getEnum("서울특별시").getShortValue())
                .build());

        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeWrongYear = localDateTimeNow.minusYears(20);
        LocalDateTime localDateTimeWrongMonth = localDateTimeNow.minusMonths(1);
        LocalDateTime localDateTimeWrongDays = localDateTimeNow.minusDays(1);

        given(memberSurveyMappingRepository.findByMemberAndSurvey(givenMember, givenSurvey)).willReturn(List.of(givenMemberSurveyMapping));
        doReturn(localDateTimeNow).when(givenMemberSurveyMapping).getCreatedAt();

        List<MemberSurveyMapping> memberRegionMappingList = mappingService.filterMemberSurveyMappingList(givenMember, givenSurvey);
        assertThat(memberRegionMappingList.get(0), is(givenMemberSurveyMapping));

        doReturn(localDateTimeWrongYear).when(givenMemberSurveyMapping).getCreatedAt();
        List<MemberSurveyMapping> memberRegionMappingWrongYearList = mappingService.filterMemberSurveyMappingList(givenMember, givenSurvey);
        assertThat(memberRegionMappingWrongYearList, is(List.of()));

        doReturn(localDateTimeWrongMonth).when(givenMemberSurveyMapping).getCreatedAt();
        List<MemberSurveyMapping> memberRegionMappingWrongMonthList = mappingService.filterMemberSurveyMappingList(givenMember, givenSurvey);
        assertThat(memberRegionMappingWrongMonthList, is(List.of()));

        doReturn(localDateTimeWrongDays).when(givenMemberSurveyMapping).getCreatedAt();
        List<MemberSurveyMapping> memberRegionMappingWrongDaysList = mappingService.filterMemberSurveyMappingList(givenMember, givenSurvey);
        assertThat(memberRegionMappingWrongDaysList, is(List.of()));
    }

    @Test
    @DisplayName("MappingService: ifAnswerExist()")
    void ifAnswerExist(){
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

        given(memberSurveyMappingRepository.findByCreatedAtAfter(any(LocalDateTime.class))).willReturn(List.of());
        assertThat(mappingService.ifAnswerExist(givenMember), is(Boolean.FALSE));
    }
}
