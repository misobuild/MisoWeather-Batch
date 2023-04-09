package com.misoweather.misoweatherservice.member.caller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("AppleAuthCallBuilder 테스트")
public class AppleAuthCallBuilderTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private AppleAuthCallBuilder appleAuthCallBuilder;

    @BeforeEach
    void setUp() {
        given(restTemplateBuilder.build()).willReturn(restTemplate);
        appleAuthCallBuilder = new AppleAuthCallBuilder(restTemplateBuilder);
    }

    @Test
    @DisplayName("AppleAuthCallBuilder addHeader() 테스트")
    void testAddHeader() {
        appleAuthCallBuilder.addHeader();
        assertThat(appleAuthCallBuilder.contentType, is("application/x-www-form-urlencoded;charset=utf-8"));
    }

    @Test
    @DisplayName("AppleAuthCallBuilder testExchange() 테스트")
    public void testExchange() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>("Mocked response", HttpStatus.OK);
        given(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                ArgumentMatchers.<Class<String>>any()))
                .willReturn(mockResponse);

        ResponseEntity<String> actual = appleAuthCallBuilder.exchange();

        assertThat(HttpStatus.OK, is(actual.getStatusCode()));
        assertEquals("Mocked response", actual.getBody());
    }
}