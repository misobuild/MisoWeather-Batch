package com.misoweather.misoweatherservice.global.utils.factory;

import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.constants.SocialType;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import com.misoweather.misoweatherservice.global.utils.validator.AppleValidator;
import com.misoweather.misoweatherservice.global.utils.validator.KakaoValidator;
import com.misoweather.misoweatherservice.global.utils.validator.Validator;

public class ValidatorFactory {
    public static Validator of(String socialId, String socialType, String socialToken){
        switch (SocialType.getEnum(socialType)){
            case KAKAO:
                return new KakaoValidator(socialId, socialToken);
            case APPLE:
                return new AppleValidator(socialId, socialToken);
            default:
                throw new ApiCustomException(HttpStatusEnum.NOT_FOUND);
        }
    }
}
