package com.misoweather.misoweatherservice.member.dto;

public class DeleteMemberRequestDtoBuilder {
    public static DeleteMemberRequestDto build(String socialId, String socialType){
        return new DeleteMemberRequestDto(socialId, socialType);
    }
}
