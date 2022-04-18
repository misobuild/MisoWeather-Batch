package com.misoweather.misoweatherservice.member.caller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.misoweather.misoweatherservice.member.builder.AppleAuthCallBuilder;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@DisplayName("AppleAuthCaller 테스트")
public class AppleAuthCallerTest {
    private ObjectMapper objectMapper;
    @Mock
    private AppleAuthCallBuilder appleAuthCallBuilder;
    @InjectMocks
    private AppleAuthCaller caller;

    @Test
    @DisplayName("KakaoAuthCaller 테스트")
    void call(){
        objectMapper = new ObjectMapper();
        RestTemplate restTemplate = spy(RestTemplate.class);
        ResponseEntity<List<String>> myEntity = ResponseEntity.ok(List.of("hello"));
        doReturn(myEntity).when(restTemplate).exchange(anyString(), eq(HttpMethod.GET), eq(null), eq(String.class));
        appleAuthCallBuilder.url = "testURL";
        appleAuthCallBuilder.restTemplate = restTemplate;
        JSONObject actual = new AppleAuthCaller(appleAuthCallBuilder).call();

        // then
        assertThat(actual.toString(), is(""));
    }
}
