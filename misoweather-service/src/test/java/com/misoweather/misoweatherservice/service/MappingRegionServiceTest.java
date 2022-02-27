package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.constants.RegionEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import com.misoweather.misoweatherservice.mapping.service.MappingRegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@DisplayName("MappingRegionService 테스트")
public class MappingRegionServiceTest {

    @Mock
    private MemberRegionMappingRepository memberRegionMappingRepository;
    @InjectMocks
    private MappingRegionService mappingRegionService;

    @BeforeEach
    void setUp(){
        this.mappingRegionService = new MappingRegionService(memberRegionMappingRepository);
    }

    @Test
    @DisplayName("성공 테스트: 현재 날짜 조건에 부합해야 하는 필터링 후, 반환된 <MemberRegionMapping> 리스트를 확인한다.")
    void filterMemberRegionMappingList() {
        MemberRegionMapping memberRegionMapping = MemberRegionMapping.builder()
                .regionStatus(RegionEnum.DEFAULT)
                .build();
        List<MemberRegionMapping> memberRegionMappingList = List.of(memberRegionMapping);

        MemberRegionMapping filteredMemberRegionMapping = mappingRegionService.filterMemberRegionMappingList(memberRegionMappingList);

        assertThat(filteredMemberRegionMapping, is(memberRegionMappingList.get(0)));
    }

    @Test
    @DisplayName("실패 테스트: 현재 날짜 조건에 부합해야 하는 필터링 후, 반환된 <MemberRegionMapping> 리스트가 비어있을 때 에러가 발생한다.")
    void filterMemberRegionMappingListFail() {
        // given
        List<MemberRegionMapping> memberRegionMappingEmptyList = List.of();

        // when, then
        assertThatThrownBy(() -> mappingRegionService.filterMemberRegionMappingList(memberRegionMappingEmptyList))
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.NOT_FOUND.getMessage());
    }

    // 성공 실패 분리
    @Test
    @DisplayName("성공 테스트: <MemberRegionMapping>리스트로부터 filter 조건에 맞는 매핑을 찾아 해당 객체의 (String)bigScale을 리턴한다.")
    void getBigScale(){
        // given
        Region givenRegion = spy(Region.class);
        doReturn("서울특별시").when(givenRegion).getBigScale();
        Member givenMember = spy(Member.class);
        MemberRegionMapping memberRegionMapping = MemberRegionMapping.builder()
                .region(givenRegion)
                .regionStatus(RegionEnum.DEFAULT)
                .build();

        given(memberRegionMappingRepository.findMemberRegionMappingByMember(givenMember)).willReturn(List.of(memberRegionMapping));

        // when
        String bigScale = mappingRegionService.getBigScale(givenMember);

        // then
        assertThat(bigScale, is("서울특별시"));
    }

    @Test
    @DisplayName("실패 테스트: <MemberRegionMapping> 리스트의 filter 조건에 맞는 매핑이 없으면 실패한다.")
    void getBigScaleFail(){
        // given
        Member givenMember = spy(Member.class);

        given(memberRegionMappingRepository.findMemberRegionMappingByMember(givenMember)).willReturn(List.of());

        // when, then
        assertThatThrownBy(() -> mappingRegionService.getBigScale(givenMember))
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.NOT_FOUND.getMessage());
    }


    @Test
    @DisplayName("성공 테스트: <Member>와 <Region>으로 <MemberRegionMapping>을 빌드한다.")
    void buildMemberRegionMapping(){
        // given
        Member givenMember = Member.builder()
                .socialId("12345")
                .emoji("a")
                .nickname("행복한 가짜광대")
                .socialType("kakao")
                .build();
        Region givenRegion = spy(Region.class);

        // when
        MemberRegionMapping memberRegionMapping = mappingRegionService.buildMemberRegionMapping(givenMember, givenRegion);

        // then
        assertThat(memberRegionMapping.getMember(), is(givenMember));
        assertThat(memberRegionMapping.getRegion(), is(givenRegion));
        assertThat(memberRegionMapping.getRegionStatus(), is(RegionEnum.DEFAULT));
    }
}
