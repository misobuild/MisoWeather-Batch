package com.misoweather.misoweatherservice.comment.service;

import com.misoweather.misoweatherservice.comment.dto.CommentListResponseDto;
import com.misoweather.misoweatherservice.comment.dto.CommentRegisterRequestDto;
import com.misoweather.misoweatherservice.comment.dto.CommentRegisterResponseDto;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.mapping.service.MappingRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleCommentService {

    private final MappingRegionService mappingRegionService;
    private final CommentService commentService;

    public CommentRegisterResponseDto registerComment(CommentRegisterRequestDto commentRegisterRequestDto, Member member){
        String bigScale = mappingRegionService.getBigScale(member);
        commentService.saveComment(commentRegisterRequestDto.getContent(), member, bigScale);
        return commentService.getAllCommentList();
    }

    public CommentListResponseDto getCommentList(Long commentId, Pageable page){
        List<Comment> rawCommentList = commentService.getComments(commentId, page);
        Long lasIdOfList = commentService.getLastId(rawCommentList);
        return new CommentListResponseDto(rawCommentList, commentService.hasNext(lasIdOfList));
    }
}
