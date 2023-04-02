package com.misoweather.misoweatherservice.member.caller;

import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@AllArgsConstructor
@Component
public class AppleAuthCaller {
    // Mock 시키기 위해서, 그리고 계속 객체가 생기는 걸 방지하기 위해서 추가한 라인
    private final AppleAuthCallBuilder appleAuthCallBuilder;
    public JSONObject call() {
        appleAuthCallBuilder.addHeader();
        appleAuthCallBuilder.setHttpEntityHeader();

        try {
            ResponseEntity<String> response = appleAuthCallBuilder.exchange();
            return new JSONObject(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new ApiCustomException(HttpStatusEnum
                    .valueOf(HttpStatus.valueOf(e.getRawStatusCode()).name()));
        }
    }
}
