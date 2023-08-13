package com.misoweather.misoweatherservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.misoweather.misoweatherservice.config.SecurityConfig;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.global.exception.ControllerExceptionHandler;
import com.misoweather.misoweatherservice.member.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.member.dto.*;
import com.misoweather.misoweatherservice.member.service.MemberService;
import com.misoweather.misoweatherservice.member.service.SimpleMemberService;
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

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("MemberController 테스트")
public class MemberControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private MemberController memberController;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private MemberService memberService;
    @MockBean
    private SimpleMemberService simpleMemberService;

    @BeforeAll
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(memberController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("getMember() 테스트")
    public void getMember() throws Exception {
        // given
        MemberInfoResponseDto memberInfoResponseDto = MemberInfoResponseDto.builder().build();
        given(simpleMemberService.getMemberInfo(any())).willReturn(memberInfoResponseDto);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/misoweather-service/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("registerUser 테스트")
    public void registerUser() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SingUpRequestDtoBuilder
                .build("test", "testType", "hello", ":)", 999L);

        Member givenMember = spy(Member.class);
        doReturn(9999L).when(givenMember).getMemberId();
        doReturn(signUpRequestDto.getSocialId()).when(givenMember).getSocialId();
        doReturn(signUpRequestDto.getSocialType()).when(givenMember).getSocialType();

        given(simpleMemberService.registerMember(any(SignUpRequestDto.class), anyString())).willReturn(givenMember);
        given(jwtTokenProvider.createToken(anyString(), anyString(), anyString())).willReturn("testServerToken");

        // when
        ResultActions result = this.mockMvc.perform(
                post("/misoweather-service/member")
                        .content(objectMapper.writeValueAsString(signUpRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("socialToken", "testToken")
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().string("serverToken", "testServerToken"));
    }

    @Test
    @DisplayName("성공: reissue() 테스트")
    public void reissue() throws Exception{
        // given
        LoginRequestDto loginRequestDto = LoginRequestDtoBuilder.build("testSocialId", "testSocialType");
        String socialToken = "testSocialToken";
        given(simpleMemberService.reissue(any(LoginRequestDto.class), anyString())).willReturn("testServerToken");

        // when
        ResultActions result = this.mockMvc.perform(
                post("/misoweather-service/member/token")
                        .content(objectMapper.writeValueAsString(loginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("socialToken", socialToken)
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().string("serverToken", "testServerToken"))
                .andDo(print());
    }

    @Test
    @DisplayName("성공: buildNickName 사용 가능 닉네임 조회하기 테스트")
    public void buildNickname() throws Exception{
        // given
        NicknameResponseDto nicknameResponseDto = NicknameResponseDto.builder()
                .nickname("testNickname")
                .emoji(":)")
                .build();

        given(memberService.buildNickname()).willReturn(nicknameResponseDto);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/misoweather-service/member/nickname")
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickname").value(equalTo("testNickname")))
                .andExpect(jsonPath("$.data.emoji").value(equalTo(":)")));
    }

    @Test
    @DisplayName("성공: delete() 회원 삭제 테스트")
    public void deleteMember() throws Exception{
        // given
        DeleteMemberRequestDto deleteMemberRequestDto = DeleteMemberRequestDtoBuilder.build("testSocialId", "testSocialType");
        willDoNothing().given(simpleMemberService).deleteMember(deleteMemberRequestDto);

        // when
        ResultActions result = this.mockMvc.perform(
                delete("/misoweather-service/member")
                        .content(objectMapper.writeValueAsString(deleteMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(equalTo("Deletion Successful")));
    }

    @Test
    @DisplayName("성공: checkExistence() 회원 가입 여부 확인")
    public void checkExistence() throws Exception {
        // given
        String givenSocialId = "testSocialId";
        String givenSocialType = "testSocialType";
        given(memberService.ifMemberExistDelete(givenSocialId, givenSocialType)).willReturn(Boolean.TRUE);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/misoweather-service/member/existence")
                        .param("socialId", givenSocialId)
                        .param("socialType", givenSocialType)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(equalTo(Boolean.TRUE)));
    }

    @Test
    @DisplayName("성공: status() is 200")
    public void healthCheck() throws Exception{
        // when
        ResultActions result = this.mockMvc.perform(
                get("/health_check")
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk());
    }
}