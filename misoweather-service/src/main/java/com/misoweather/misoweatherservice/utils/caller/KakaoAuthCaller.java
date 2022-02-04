package com.misoweather.misoweatherservice.utils.caller;

import com.misoweather.misoweatherservice.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.exception.ApiCustomException;
import com.misoweather.misoweatherservice.utils.builder.KakaoAuthCallBuilder;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

@AllArgsConstructor
public class KakaoAuthCaller implements ApiCaller {
    String socialToken;

    public JSONObject call() {
        KakaoAuthCallBuilder kakaoAuthCallBuilder = new KakaoAuthCallBuilder(socialToken);
        kakaoAuthCallBuilder.addHeader();
        kakaoAuthCallBuilder.setHttpEntityHeader();

        try {
            ResponseEntity<String> response = kakaoAuthCallBuilder.restTemplate.exchange(
                    kakaoAuthCallBuilder.url,
                    HttpMethod.GET,
                    kakaoAuthCallBuilder.httpEntityHeader,
                    String.class);
            return new JSONObject(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new ApiCustomException(HttpStatusEnum
                    .valueOf(HttpStatus.valueOf(e.getRawStatusCode()).name()));
        }
    }
}
