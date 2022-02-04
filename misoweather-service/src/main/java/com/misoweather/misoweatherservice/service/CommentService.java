package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.constants.RegionEnum;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.comment.CommentRepository;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.dto.request.comment.CommentRegisterRequestDto;
import com.misoweather.misoweatherservice.dto.response.comment.CommentListResponseDto;
import com.misoweather.misoweatherservice.dto.response.comment.CommentRegisterResponseDto;
import com.misoweather.misoweatherservice.exception.ApiCustomException;
import com.misoweather.misoweatherservice.utils.reader.ContentReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static final int DEFAULT_SIZE = 21;

    private final CommentRepository commentRepository;
    private final MemberRegionMappingRepository memberRegionMappingRepository;
    private final ContentReader contentReader;

    public CommentRegisterResponseDto registerComment(CommentRegisterRequestDto commentRegisterRequestDto, Member member) {
        String bigScale = memberRegionMappingRepository.findMemberRegionMappingByMember(member).stream()
                .filter(item -> item.getRegionStatus().equals(RegionEnum.DEFAULT))
                .map(item -> item.getRegion().getBigScale())
                .findFirst()
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));

        Comment comment = Comment.builder()
                .content(contentReader.checker(commentRegisterRequestDto.getContent()))
                .bigScale(BigScaleEnum.getEnum(bigScale).toString())
                .member(member)
                .nickname(member.getNickname())
                .deleted(Boolean.FALSE)
                .emoji(member.getEmoji())
                .build();

        commentRepository.save(comment);

        return CommentRegisterResponseDto.builder()
                .commentList(commentRepository.findAll())
                .build();
    }

    public CommentListResponseDto getCommentList(Long commentId, Pageable page){
        final List<Comment> rawCommentList = getComments(commentId, page);
        final Long lastIdOfList = rawCommentList.isEmpty() ?
                null : rawCommentList.get(rawCommentList.size() - 1).getId();

        return new CommentListResponseDto(rawCommentList, hasNext(lastIdOfList));
    }

    private List<Comment> getComments(Long commentId, Pageable page) {
        return commentId == null ?
                this.commentRepository.findAllByOrderByIdDesc(page) :
                this.commentRepository.findByIdLessThanOrderByIdDesc(commentId, page);
    }

    private Boolean hasNext(Long id) {
        if (id == null) return false;
        return this.commentRepository.existsByIdLessThan(id);
    }

}
