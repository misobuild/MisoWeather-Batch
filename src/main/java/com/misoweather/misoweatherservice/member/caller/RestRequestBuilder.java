package com.misoweather.misoweatherservice.member.caller;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

abstract class RestRequestBuilder {
    protected String url;

    protected String bearerToken;
    protected String contentType;
    protected HttpHeaders headers;
    protected RestTemplate restTemplate;
    protected HttpEntity<MultiValueMap<String, String>> httpEntityHeader;

    abstract void addHeader();
    protected void setHttpEntityHeader(){
        httpEntityHeader = new HttpEntity<>(headers);
    }
}
