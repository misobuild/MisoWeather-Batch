package com.misoweather.misoweatherservice.member.validator;

import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.constants.SocialType;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidatorFactory {

    public Validator of(String socialId, String socialType, String socialToken){
        switch (SocialType.getEnum(socialType)){
            case KAKAO:
                kakaoValidator.setIdAndToken(socialId, socialToken);
                return kakaoValidator;
            case APPLE:
                appleValidator.setIdAndToken(socialId, socialToken);
                return appleValidator;
            default:
                throw new ApiCustomException(HttpStatusEnum.NOT_FOUND);
        }
    }
}
