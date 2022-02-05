package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.dto.request.comment.CommentRegisterRequestDto;
import com.misoweather.misoweatherservice.dto.response.comment.CommentRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainFlowService {

    private final MemberService memberService;
    private final CommentService commentService;
    private final RegionService regionService;
    private final SurveyService surveyService;

    public CommentRegisterResponseDto registerComment(CommentRegisterRequestDto commentRegisterRequestDto, Member member){
        String bigScale = memberService.getBigScale(member);
        commentService.saveComment(commentRegisterRequestDto, member, bigScale);
        return commentService.getAllCommentList();
    }
}
