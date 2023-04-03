package com.misoweather.misoweatherservice.member.validator;

import com.misoweather.misoweatherservice.member.caller.KakaoAuthCaller;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KakaoValidator extends Validator{
    String socialId;
    String socialToken;
    private final KakaoAuthCaller kakaoAuthCaller;

    @Override
    public Boolean valid() {
        kakaoAuthCaller.setSocialToken(socialToken);
        JSONObject jsonObject = kakaoAuthCaller.call();
        if(jsonObject.get("id").toString().equals(socialId)) return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public void setIdAndToken(String socialId, String socialToken) {
        this.socialId = socialId;
        this.socialToken = socialToken;
    }
}
