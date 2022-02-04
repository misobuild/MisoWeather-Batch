package com.misoweather.misoweatherservice.utils.factory;

import com.misoweather.misoweatherservice.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.constants.SocialType;
import com.misoweather.misoweatherservice.exception.ApiCustomException;
import com.misoweather.misoweatherservice.utils.validator.AppleValidator;
import com.misoweather.misoweatherservice.utils.validator.KakaoValidator;
import com.misoweather.misoweatherservice.utils.validator.Validator;

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
