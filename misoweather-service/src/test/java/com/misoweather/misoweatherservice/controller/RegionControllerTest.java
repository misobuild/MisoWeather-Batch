package com.misoweather.misoweatherservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.misoweather.misoweatherservice.config.SecurityConfig;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.domain.region.RegionBuilder;
import com.misoweather.misoweatherservice.global.exception.ControllerExceptionHandler;
import com.misoweather.misoweatherservice.member.auth.JwtTokenProvider;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        Region region = RegionBuilder.build(99999L, "경기도", "고양시덕양구", "행신1동");
        RegionResponseDto givenRegionresponseDto = RegionResponseDto.builder().midScaleList(List.of(region)).build();
        given(regionService.getMidScaleList(any())).willReturn(givenRegionresponseDto);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/region/{bigScaleRegion}", "hello")
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.regionList[0].bigScale").value(equalTo("경기도")))
                .andExpect(jsonPath("$.data.regionList[0].midScale").value(equalTo("고양시덕양구")))
                .andExpect(jsonPath("$.data.regionList[0].smallScale").value(equalTo("행신1동")))
                .andDo(print());
    }
}
