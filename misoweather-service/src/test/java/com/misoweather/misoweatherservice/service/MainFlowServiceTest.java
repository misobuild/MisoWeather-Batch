package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.dto.request.comment.CommentRegisterRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService 테스트")
public class MainFlowServiceTest {

    @Mock private CommentService commentService;
    @Mock private MemberService memberService;

    @Test
    @DisplayName("MainFlowService: registerComment 테스트")
    void registerCommentTest(){
        //given
        String content = "안녕하세요";
        Member member = Member.builder()
                .socialId("12345")
                .emoji("a")
                .nickname("행복한 가짜광대")
                .socialType("kakao")
                .build();

        //when
        given(memberService.getBigScale(member)).willReturn("서울특별시");


        //then
    }

}
