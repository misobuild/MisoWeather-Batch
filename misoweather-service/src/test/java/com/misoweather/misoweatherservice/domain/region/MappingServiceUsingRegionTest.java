package com.misoweather.misoweatherservice.domain.region;

import com.misoweather.misoweatherservice.constants.RegionEnum;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMappingRepository;
import com.misoweather.misoweatherservice.exception.ApiCustomException;
import com.misoweather.misoweatherservice.service.MappingService;
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@DisplayName("Default Region 생성자를 필요로하는 MappingServiceTest")
public class MappingServiceUsingRegionTest {

    @Mock private MemberSurveyMappingRepository memberSurveyMappingRepository;
    @Mock private MemberRegionMappingRepository memberRegionMappingRepository;
    @InjectMocks private MappingService mappingService;

    @BeforeEach
    void setUp() {
            this.mappingService = new MappingService(memberRegionMappingRepository, memberSurveyMappingRepository);
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

    @Test
    @DisplayName("MappingService: getBigScale()")
    void getBigScale(){
        Member givenMember = Member.builder()
                .build();

        Region givenRegion = spy(Region.class);

        MemberRegionMapping memberRegionMapping = MemberRegionMapping.builder()
                .region(givenRegion)
                .regionStatus(RegionEnum.DEFAULT)
                .build();

        given(memberRegionMappingRepository.findMemberRegionMappingByMember(givenMember)).willReturn(List.of(memberRegionMapping));

        String bigScale = mappingService.getBigScale(givenMember);
        ApiCustomException exceptionThrown = Assertions.assertThrows(
                ApiCustomException.class,
                () -> {
                    mappingService.getBigScale(null);
                }
        );

        assertThat(bigScale, is("서울특별시"));
        assertThat(exceptionThrown.getMessage(), is("NOT_FOUND"));
    }

    @Test
    @DisplayName("MappingService: buildMemeberRegionMapping()")
    void buildMemberRegionMapping(){
        // given
        Member givenMember = Member.builder()
                .socialId("12345")
                .emoji("a")
                .nickname("행복한 가짜광대")
                .socialType("kakao")
                .build();

        Region givenRegion =
                new Region("서울특별시", "중구", "신당동", 1, 1, 1, 1, LocalDateTime.now());

        MemberRegionMapping memberRegionMappingShouldBe = MemberRegionMapping.builder()
                .regionStatus(RegionEnum.DEFAULT)
                .member(givenMember)
                .region(givenRegion)
                .build();

        MemberRegionMapping memberRegionMapping = mappingService.buildMemberRegionMapping(givenMember, givenRegion);

        assertThat(memberRegionMapping.getMember(), is(givenMember));
        assertThat(memberRegionMapping.getRegion(), is(givenRegion));
        assertThat(memberRegionMapping.getRegionStatus(), is(RegionEnum.DEFAULT));
    }
}