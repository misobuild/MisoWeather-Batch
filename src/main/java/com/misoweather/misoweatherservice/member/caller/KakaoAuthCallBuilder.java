package com.misoweather.misoweatherservice.member.caller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class KakaoAuthCallBuilder extends RestRequestBuilder {
    private String kakaoTokenCheckURL = "https://kapi.kakao.com/v1/user/access_token_info";
    private final String contentTypeValue = "application/x-www-form-urlencoded;charset=utf-8";

    @Override
    public void addHeader() {
        headers.add("Authorization", "Bearer " + bearerToken);
        headers.add("Content-type", contentTypeValue);
    }

    public void setSocialToken(String socialToken) {
        this.bearerToken = socialToken;
    }

    public ResponseEntity<String> exchange() {
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntityHeader,
                String.class);
    }

    @Autowired
    KakaoAuthCallBuilder(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.headers = new HttpHeaders();
        this.contentType = kakaoTokenCheckURL;
        this.url = kakaoTokenCheckURL;
    }
}
