package com.misoweather.misoweatherservice.global.utils.caller;

import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import com.misoweather.misoweatherservice.global.utils.builder.AppleAuthCallBuilder;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

@AllArgsConstructor
public class AppleAuthCaller implements ApiCaller {
    public JSONObject call() {
        AppleAuthCallBuilder appleAuthCallBuilder = new AppleAuthCallBuilder();
        appleAuthCallBuilder.addHeader();
        appleAuthCallBuilder.setHttpEntityHeader();

        try {
            ResponseEntity<String> response = appleAuthCallBuilder.restTemplate.exchange(
                    appleAuthCallBuilder.url,
                    HttpMethod.GET,
                    appleAuthCallBuilder.httpEntityHeader,
                    String.class);
            return new JSONObject(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new ApiCustomException(HttpStatusEnum
                    .valueOf(HttpStatus.valueOf(e.getRawStatusCode()).name()));
        }
    }
}
