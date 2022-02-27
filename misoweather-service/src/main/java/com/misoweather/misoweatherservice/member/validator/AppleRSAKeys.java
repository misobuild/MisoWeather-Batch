package com.misoweather.misoweatherservice.member.validator;

import lombok.Getter;

import java.util.List;

@Getter
public class AppleRSAKeys {

    private List<AppleRSAKey> keys;

    @Getter
    public static class AppleRSAKey {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;
    }
}
