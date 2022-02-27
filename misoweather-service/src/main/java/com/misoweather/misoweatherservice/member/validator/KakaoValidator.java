package com.misoweather.misoweatherservice.member.validator;

import com.misoweather.misoweatherservice.member.caller.KakaoAuthCaller;
import lombok.AllArgsConstructor;
import org.json.JSONObject;

@AllArgsConstructor
public class KakaoValidator implements Validator{
    String socialId;
    String socialToken;

    public Boolean valid() {
        KakaoAuthCaller kakaoAuthCaller = new KakaoAuthCaller(socialToken);
        JSONObject jsonObject = kakaoAuthCaller.call();
        if(jsonObject.get("id").toString().equals(socialId)) return Boolean.TRUE;
        return Boolean.FALSE;
    }
}
