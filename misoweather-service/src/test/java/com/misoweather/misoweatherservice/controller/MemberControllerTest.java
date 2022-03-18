package com.misoweather.misoweatherservice.controller;

import com.misoweather.misoweatherservice.config.WithCustomMockUser;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.member.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.member.auth.UserDetailsImpl;
import com.misoweather.misoweatherservice.member.dto.MemberInfoResponseDto;
import com.misoweather.misoweatherservice.member.service.MemberService;
import com.misoweather.misoweatherservice.member.service.SimpleMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@DisplayName("MemberController 테스트")
@Import(SecurityContextPersistenceFilter.class)
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private MemberService memberService;
    @MockBean
    private UserDetailsImpl userDetailsImpl;
    @MockBean
    private SimpleMemberService simpleMemberService;

    private Collection<? extends GrantedAuthority> authorities(String userRole){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userRole));
        return authorities;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("Mock User get Member 테스트")
    public void getMember() throws Exception {
        // given
        Member givenMember = spy(Member.class);
        MemberInfoResponseDto memberInfoResponseDto = MemberInfoResponseDto.builder().build();
        Authentication authentication = new UsernamePasswordAuthenticationToken("test", "test", authorities("USER"));
        given(jwtTokenProvider.resolveToken(any())).willReturn("helloToken");
        given(jwtTokenProvider.validateToken(any())).willReturn(Boolean.TRUE);
        given(jwtTokenProvider.getAuthentication(anyString())).willReturn(authentication);
        given(userDetailsImpl.getMember()).willReturn(givenMember);
        given(simpleMemberService.getMemberInfo(any())).willReturn(memberInfoResponseDto);


        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/member")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andDo(print());
    }
}