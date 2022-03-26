package com.misoweather.misoweatherservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.misoweather.misoweatherservice.config.SecurityConfig;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.domain.region.RegionBuilder;
import com.misoweather.misoweatherservice.global.exception.ControllerExceptionHandler;
import com.misoweather.misoweatherservice.member.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.member.auth.UserDetailsImpl;
import com.misoweather.misoweatherservice.region.dto.RegionResponseDto;
import com.misoweather.misoweatherservice.region.service.RegionService;
import com.misoweather.misoweatherservice.region.service.SimpleRegionService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegionController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("MemberController 테스트")
public class RegionControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private RegionController regionController;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RegionService regionService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private SimpleRegionService simpleRegionService;
    @MockBean
    private UserDetailsImpl userDetails;

    @BeforeAll
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(regionController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("성공: getMidScaleList()")
    public void getMidScaleList() throws Exception{
        // given
        Region givenRegion = RegionBuilder.build(99999L, "경기도", "고양시덕양구", "행신1동");
        RegionResponseDto givenRegionresponseDto = RegionResponseDto.builder().midScaleList(List.of(givenRegion)).build();
        given(regionService.getMidScaleList(anyString())).willReturn(givenRegionresponseDto);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/region/{bigScaleRegion}", "경기도")
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.regionList[0].bigScale").value(equalTo("경기도")))
                .andExpect(jsonPath("$.data.regionList[0].midScale").value(equalTo("고양시덕양구")))
                .andExpect(jsonPath("$.data.regionList[0].smallScale").value(equalTo("행신1동")))
                .andDo(print());
    }

    @Test
    @DisplayName("성공: getSmallScaleList() 테스트")
    public void getSmallScaleList() throws Exception{
        // given
        Region givenRegion = RegionBuilder.build(99999L, "경기도", "고양시덕양구", "행신1동");
        RegionResponseDto givenRegionresponseDto = RegionResponseDto.builder().midScaleList(List.of(givenRegion)).build();
        given(regionService.getSmallScaleList(anyString(), anyString())).willReturn(givenRegionresponseDto);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/region/{bigScaleRegion}/{midScaleRegion}", givenRegion.getBigScale(), givenRegion.getMidScale())
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(equalTo("bigScale과 midScale로 찾아온 smallScale 리스트")))
                .andExpect(jsonPath("$.data.regionList[0].bigScale").value(equalTo("경기도")))
                .andExpect(jsonPath("$.data.regionList[0].midScale").value(equalTo("고양시덕양구")))
                .andExpect(jsonPath("$.data.regionList[0].smallScale").value(equalTo("행신1동")))
                .andDo(print());
    }

    @Test
    @DisplayName("성공: updateMemberRegion() 테스트")
    public void updateMemberRegion() throws Exception{
        // given
        Member givenSpyMember = spy(Member.class);
        Region givenRegion = RegionBuilder.build(99999L, "경기도", "고양시덕양구", "행신1동");
        MemberRegionMapping givenMemberRegionMapping = MemberRegionMapping.builder().region(givenRegion).build();

        given(userDetails.getMember()).willReturn(givenSpyMember);
        given(simpleRegionService.updateRegion(any(), anyLong())).willReturn(givenMemberRegionMapping);

        // when
        ResultActions result = this.mockMvc.perform(
                put("/api/member-region-mapping/default")
                        .param("regionId", String.valueOf(99999L))
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(equalTo(99999)))
                .andDo(print());
    }


}
