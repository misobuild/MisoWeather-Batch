package com.misoweather.misoweatherservice.region.service;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.domain.region.RegionRepository;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.constants.RegionEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import com.misoweather.misoweatherservice.region.dto.RegionResponseDto;
import com.misoweather.misoweatherservice.region.reader.RegionReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegionService 테스트")
public class RegionServiceTest {
    @Mock
    private RegionRepository regionRepository;
    @Mock
    private RegionReader regionReader;
    @InjectMocks
    private RegionService regionService;

    // JPA 테스트 필요
    @Test
    @DisplayName("getRegion() 테스트")
    void getRegionTest(){

    }

    @Test
    @DisplayName("getMidScaleList() 테스트")
    void getMidScaleListTest(){
        // given
        String givenRegionName = "경기";
        Region givenRegion = spy(Region.class);
        doReturn(9999L).when(givenRegion).getId();

        given(regionRepository.findByBigScale(anyString())).willReturn(List.of(givenRegion));
        given(regionReader.filterMidScaleList(anyList())).willReturn(List.of(givenRegion));

        // when
        RegionResponseDto actual = regionService.getMidScaleList(givenRegionName);

        // then
        assertThat(actual.getRegionList().get(0).getId(), is(9999L));

    }

    @Test
    @DisplayName("getSmallScaleList() 테스트")
    void getSmallScaleListTest(){
        // given
        String bigScale = "경기도";
        String midScale = "고양시덕양구";
        Region givenRegion = spy(Region.class);
        doReturn(9999L).when(givenRegion).getId();

        given(regionRepository.findByBigScaleAndMidScale(bigScale, midScale)).willReturn(List.of(givenRegion));

        // when
        RegionResponseDto actual = regionService.getSmallScaleList(bigScale, midScale);

        // then
        assertThat(actual.getRegionList().get(0).getId(), is(9999L));
    }

    @Test
    @DisplayName("updateEach() 테스트 리스트에 값이 있는 경우")
    void updateEachTest(){
        Region givenRegion = spy(Region.class);
        doReturn(9999L).when(givenRegion).getId();

        MemberRegionMapping givenMemberRegionMapping = MemberRegionMapping.builder()
                .regionStatus(RegionEnum.DEFAULT)
                .build();

        List<MemberRegionMapping> givenList = new ArrayList<>();
        givenList.add(givenMemberRegionMapping);

        // when
        MemberRegionMapping actual = regionService.updateEach(givenList, givenRegion);

        assertThat(actual.getRegion().getId(), is(9999L));
    }

    @Test
    @DisplayName("updateEach() 테스트 리스트에 값이 없는 경우")
    void updateEachTestFail(){
        Region givenRegion = spy(Region.class);
        List<MemberRegionMapping> givenList = new ArrayList<>();

        // when, then
        assertThatThrownBy(() -> regionService.updateEach(givenList, givenRegion))
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.CONFLICT.getMessage());
    }
}
