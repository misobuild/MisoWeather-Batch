package com.misoweather.misoweatherservice.member.caller;

import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AppleAuthCaller 테스트")
public class AppleApiCallerTest {

    @Mock
    private AppleAuthCallBuilder appleAuthCallBuilder;

    private AppleAuthCaller appleAuthCaller;

    @BeforeEach
    void setUp() {
        appleAuthCaller = new AppleAuthCaller(appleAuthCallBuilder);
    }

    @Test
    @DisplayName("call() 성공 테스트")
    void testCallSuccess() {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> mockResponse = ResponseEntity.ok("{\"key\": \"value\"}");

        given(appleAuthCallBuilder.exchange()).willReturn(mockResponse);

        JSONObject result = appleAuthCaller.call();

        assertThat("value", is(result.getString("key")));

        verify(appleAuthCallBuilder, times(1)).addHeader();
        verify(appleAuthCallBuilder, times(1)).setHttpEntityHeader();
        verify(appleAuthCallBuilder, times(1)).exchange();
    }

    @Test
    @DisplayName("call() 실패 테스트")
    void testCallFailure() {
        HttpClientErrorException mockException = new HttpClientErrorException(HttpStatus.NOT_FOUND);

        given(appleAuthCallBuilder.exchange()).willThrow(mockException);

        assertThatThrownBy(() -> appleAuthCaller.call())
                .isInstanceOf(ApiCustomException.class)
                .hasMessageContaining(HttpStatusEnum.NOT_FOUND.getMessage());

        verify(appleAuthCallBuilder, times(1)).addHeader();
        verify(appleAuthCallBuilder, times(1)).setHttpEntityHeader();
        verify(appleAuthCallBuilder, times(1)).exchange();
    }
}
