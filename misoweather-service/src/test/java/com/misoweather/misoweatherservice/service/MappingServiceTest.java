package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.constants.RegionEnum;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMappingRepository;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.exception.ApiCustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
    @DisplayName("filterMemberRegionMappingList(): 현재 날짜와 같은 mapping만 남도록 filter 한다.")
    void filterMemberRegionMappingList() {
        // given
        Member givenMember = Member.builder()
                .socialId("12345")
                .emoji("a")
                .nickname("행복한 가짜광대")
                .socialType("kakao")
                .build();

        Region givenRegion = Region.builder()
                .bigScale("서울특별시")
                .midScale("중구")
                .smallScale("신당동")
                .id(28L)
                .lastWeatherUpdate(null)
                .longitude(127)
                .latitude(37)
                .LOCATION_X(60)
                .LOCATION_Y(127)
                .build();

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
