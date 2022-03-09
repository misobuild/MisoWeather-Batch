package com.misoweather.misoweatherservice.gloabl.api;

import com.misoweather.misoweatherservice.global.api.ApiResponseWithData;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ApiResponseWithDataTest {
    @Test
    @DisplayName("성공: <ApiResponseWithData> 객체를 빌드한다.")
    void build(){
        // given, when
        ApiResponseWithData actual = ApiResponseWithData.builder()
                .data("행복하세요")
                .message("hello")
                .status(HttpStatusEnum.OK)
                .build();

        // then
        assertThat(actual.getData(), is("행복하세요"));
        assertThat(actual.getMessage(), is("hello"));
        assertThat(actual.getStatus(), is(HttpStatusEnum.OK));
    }
}
