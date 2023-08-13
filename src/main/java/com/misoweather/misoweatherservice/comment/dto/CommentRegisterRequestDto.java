package com.misoweather.misoweatherservice.comment.dto;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentRegisterRequestDto {
    @NotNull
    private String content;

    CommentRegisterRequestDto(String content) {
        this.content = content;
    }
}

