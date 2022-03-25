package com.misoweather.misoweatherservice.comment.dto;

public class CommentRegisterRequestDtoBuilder {
    public static CommentRegisterRequestDto build(String content){
        return new CommentRegisterRequestDto(content);
    }
}
