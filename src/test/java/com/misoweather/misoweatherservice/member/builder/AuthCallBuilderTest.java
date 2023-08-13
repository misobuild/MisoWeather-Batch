package com.misoweather.misoweatherservice.member.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthCallBuilder 테스트")
public class AuthCallBuilderTest {

    private RestTemplateBuilder builder;

    @Test
    @DisplayName("KAKAO setHttpEntityHeader() 테스트")
    void kakaoSetHttpEntityHeaderTest(){
        // given
        String testToken = "testToken";
        String contentTypeValue = "application/x-www-form-urlencoded;charset=utf-8";

        // when
        builder = new KakaoAuthCallBuilder(testToken);
        builder.addHeader();
        builder.setHttpEntityHeader();

        // then
        assertThat(builder.contentType, is(contentTypeValue));
        assertThat(Objects.requireNonNull(builder.httpEntityHeader.getHeaders().get("Content-type")).get(0), is(contentTypeValue));
    }


    @Test
    @DisplayName("APPLE setHttpEntityHeader() 테스트")
    void appleSetHttpEntityHeaderTest(){
        // given
        String contentTypeValue = "application/x-www-form-urlencoded;charset=utf-8";

        // when
        builder = new AppleAuthCallBuilder();
        builder.addHeader();
        builder.setHttpEntityHeader();

        // then
        assertThat(Objects.requireNonNull(builder.headers.get("Content-type")).get(0), is(contentTypeValue));
        assertThat(Objects.requireNonNull(builder.httpEntityHeader.getHeaders().get("Content-type")).get(0), is(contentTypeValue));
    }
}
