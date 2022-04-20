package com.misoweather.misoweatherservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.misoweather.misoweatherservice.comment.service.SimpleCommentService;
import com.misoweather.misoweatherservice.config.SecurityConfig;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import com.misoweather.misoweatherservice.global.exception.ControllerExceptionHandler;
import com.misoweather.misoweatherservice.member.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.member.auth.UserDetailsImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ControllerAdvice 테스트")
public class ControllerAdviceTest {
    private MockMvc mockMvc;
    @MockBean
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
    public void 가져올_코멘트_리스트가_없을_때_Exception_잘_반환하는지() throws Exception{
        // given
        doThrow(new ApiCustomException(HttpStatusEnum.BAD_REQUEST)).when(commentController).getCommentList(any(), any());

        // when
        ResultActions result = this.mockMvc.perform(
                get("/api/comment")
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON));
        // then
        assertThat(getApiResultExceptionClass(
                result
                        .andExpect(status().isBadRequest())
                        .andReturn()), is(ApiCustomException.class));
    }

    private Class<? extends Exception> getApiResultExceptionClass(MvcResult result) {
        return Objects.requireNonNull(result.getResolvedException()).getClass();
    }
}
