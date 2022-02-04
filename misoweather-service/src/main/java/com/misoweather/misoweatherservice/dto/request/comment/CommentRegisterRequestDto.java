package com.misoweather.misoweatherservice.dto.request.comment;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class CommentRegisterRequestDto {
    @NotNull
    private String content;
}

