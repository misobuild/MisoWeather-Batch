package com.misoweather.misoweatherservice.config;

import com.misoweather.misoweatherservice.domain.member.Member;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.mockito.Mockito.spy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class)
public @interface WithCustomMockUser {
    String email() default "gildong@gmail.com";
    String role() default "USER";
}
