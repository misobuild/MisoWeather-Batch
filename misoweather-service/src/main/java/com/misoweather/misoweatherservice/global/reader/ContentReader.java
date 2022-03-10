package com.misoweather.misoweatherservice.global.reader;

import org.springframework.stereotype.Component;

@Component
public class ContentReader {
    public String check(String content){
        return content.replaceAll("(\r\n|\r|\n|\n\r)", " ");
    }
}
