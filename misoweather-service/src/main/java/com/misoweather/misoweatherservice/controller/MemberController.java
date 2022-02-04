package com.misoweather.misoweatherservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class MemberController {
    private final Environment env;

    @GetMapping("/health_check")
    public String status(){
        return String.format("It's working in Misoweather Service"
                + " on port " + env.getProperty("local.server.port")
                + " with tokenSecret " + env.getProperty("app.auth.tokenSecret")
        );
    }

}
