package com.misoweather.misoweatherservice.member.caller;

import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
@AllArgsConstructor
public class KakaoAuthCaller {
    protected String socialToken;
    private final KakaoAuthCallBuilder kakaoAuthCallBuilder;

    public JSONObject call() {
        kakaoAuthCallBuilder.addHeader();
        kakaoAuthCallBuilder.setHttpEntityHeader();
        kakaoAuthCallBuilder.setSocialToken(socialToken);

        try {
            ResponseEntity<String> response = kakaoAuthCallBuilder.exchange();
            return new JSONObject(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new ApiCustomException(HttpStatusEnum
                    .valueOf(HttpStatus.valueOf(e.getRawStatusCode()).name()));
        }
    }
}
