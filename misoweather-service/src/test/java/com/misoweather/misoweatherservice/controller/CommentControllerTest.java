package com.misoweather.misoweatherservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.misoweather.misoweatherservice.comment.dto.CommentListResponseDto;
import com.misoweather.misoweatherservice.comment.dto.CommentRegisterRequestDto;
import com.misoweather.misoweatherservice.comment.dto.CommentRegisterRequestDtoBuilder;
import com.misoweather.misoweatherservice.comment.dto.CommentRegisterResponseDto;
import com.misoweather.misoweatherservice.comment.service.SimpleCommentService;
import com.misoweather.misoweatherservice.config.SecurityConfig;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.global.exception.ControllerExceptionHandler;
import com.misoweather.misoweatherservice.member.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.member.auth.UserDetailsImpl;
import org.junit.jupiter.api.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CommentController 테스트")
public class CommentControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private CommentController commentController;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SimpleCommentService simpleCommentService;
    @MockBean
    private UserDetailsImpl userDetails;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeAll
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(commentController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("성공: registerComment() 코멘트 등록한다")
    public void registerComment() throws Exception{
        // given
        Member givenMember = spy(Member.class);
        CommentRegisterRequestDto givenCommentRegisterRequestDto = CommentRegisterRequestDtoBuilder.build("안녕하세요");
        Comment givenComment = Comment.builder().content(givenCommentRegisterRequestDto.getContent()).build();
        CommentRegisterResponseDto commentRegisterResponseDto = CommentRegisterResponseDto.builder().commentList(List.of(givenComment)).build();

        given(userDetails.getMember()).willReturn(givenMember);
        given(simpleCommentService.registerComment(any(CommentRegisterRequestDto.class), any())).willReturn(commentRegisterResponseDto);

        // when
        ResultActions result = this.mockMvc.perform(
                post("/api/comment")
                        .content(objectMapper.writeValueAsString(givenCommentRegisterRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.commentList[0].content").value(equalTo("안녕하세요")))
                .andDo(print());
    }

    @Test
    @DisplayName("성공: getCommentList() 코멘트 조회 - commentId 있는 경우")
    public void getCommentList() throws Exception{
        // given
        Comment givenComment = Comment.builder().content("안녕하세요").build();
        CommentListResponseDto givenCommentListResponseDto = CommentListResponseDto.builder()
                .commentList(List.of(givenComment))
                .build();
        given(simpleCommentService.getCommentList(any(), any())).willReturn(givenCommentListResponseDto);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/comment")
                        .param("commentId", String.valueOf(99L))
                        .param("size", String.valueOf(1))
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.commentList[0].content").value(equalTo("안녕하세요")))
                .andDo(print());
    }


    @Test
    @DisplayName("성공: getCommentList() 코멘트 조회 - commentId 없는 경우")
    public void getCommentListWhenCommentIdSizeNull() throws Exception{
        // given
        CommentListResponseDto givenCommentListResponseDto = CommentListResponseDto.builder().commentList(List.of()).build();
        given(simpleCommentService.getCommentList(any(), any())).willReturn(givenCommentListResponseDto);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/comment")
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON));
        // then
        result
                .andExpect(status().isOk())
                .andDo(print());
    }
}
