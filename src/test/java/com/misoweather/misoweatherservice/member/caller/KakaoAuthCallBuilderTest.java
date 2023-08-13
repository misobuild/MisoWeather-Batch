package com.misoweather.misoweatherservice.member.caller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("KakaoAuthCallBuilder 테스트")
public class KakaoAuthCallBuilderTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private KakaoAuthCallBuilder kakaoAuthCallBuilder;

    @BeforeEach
    void setUp() {
        given(restTemplateBuilder.build()).willReturn(restTemplate);
        kakaoAuthCallBuilder = new KakaoAuthCallBuilder(restTemplateBuilder);
    }

    @Test
    @DisplayName("KakaoAuthCallBuilder addHeader() 테스트")
    void testSetHttpEntityHeader() {

        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.add("Authorization", "Bearer null");
        expectedHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity expectedHttpEntityHeader = new HttpEntity<>(expectedHeaders);

        kakaoAuthCallBuilder.addHeader();
        kakaoAuthCallBuilder.setHttpEntityHeader();

        assertThat(kakaoAuthCallBuilder.httpEntityHeader.getHeaders(), is(expectedHeaders));
        assertThat(kakaoAuthCallBuilder.httpEntityHeader, is(expectedHttpEntityHeader));
    }

    @Test
    @DisplayName("KakaoAuthCallBuilder testExchange() 테스트")
    public void testExchange() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>("Mocked response", HttpStatus.OK);
        given(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                ArgumentMatchers.<Class<String>>any()))
                .willReturn(mockResponse);

        ResponseEntity<String> actual = kakaoAuthCallBuilder.exchange();

        assertThat(HttpStatus.OK, is(actual.getStatusCode()));
        assertEquals("Mocked response", actual.getBody());
    }

    @Test
    @DisplayName("KakaoAuthCallBuilder setSocialToken() 테스트")
    void testSetSocialToken(){
        String expectedToken = "testToken";
        kakaoAuthCallBuilder.setSocialToken(expectedToken);

        String actual = kakaoAuthCallBuilder.bearerToken;

        assertThat(actual, is("testToken"));
    }
}