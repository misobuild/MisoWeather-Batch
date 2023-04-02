package com.misoweather.misoweatherservice.member.caller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * identityToken의 RSA 검증을 위해 요청을 보낼 객체를 만듭니다.
 *ㅇㅈ
 * @author yeon
**/
@Component
public class AppleAuthCallBuilder extends RestRequestBuilder {
    private final String appleAuthUrl = "https://appleid.apple.com/auth/keys";
    private final String contentTypeValue = "application/x-www-form-urlencoded;charset=utf-8";

    @Override
    public void addHeader() {
        headers.add("Content-type", contentTypeValue);
    }

    /*
    SpringBoot 4.3 부터 생성자 하나인 경우 @Autowired 생략 가능하다.
     */
    @Autowired
    AppleAuthCallBuilder(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
        this.headers = new HttpHeaders();
        this.url = appleAuthUrl;
        this.contentType = contentTypeValue;
    }

    public ResponseEntity<String> exchange() {
        return restTemplate.exchange(
                appleAuthUrl,
                HttpMethod.GET,
                httpEntityHeader,
                String.class);
    }
}
