package com.misoweather.misoweatherservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.misoweather.misoweatherservice.config.SecurityConfig;
import com.misoweather.misoweatherservice.member.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.member.service.MemberService;
import com.misoweather.misoweatherservice.member.service.SimpleMemberService;
import com.misoweather.misoweatherservice.region.service.RegionService;
import com.misoweather.misoweatherservice.region.service.SimpleRegionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MemberController.class,
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
    private SimpleRegionService simpleRegionService;
}
