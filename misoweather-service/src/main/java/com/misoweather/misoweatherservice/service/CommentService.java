package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.comment.CommentRepository;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.dto.response.comment.CommentListResponseDto;
import com.misoweather.misoweatherservice.dto.response.comment.CommentRegisterResponseDto;
import com.misoweather.misoweatherservice.utils.reader.ContentReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ContentReader contentReader;

    public void saveComment(String content, Member member, String bigScale){
        Comment comment = Comment.builder()
                .content(contentReader.checker(content))
                .bigScale(BigScaleEnum.getEnum(bigScale).toString())
                .member(member)
                .nickname(member.getNickname())
                .deleted(Boolean.FALSE)
                .emoji(member.getEmoji())
                .build();
        commentRepository.save(comment);
    }

    public CommentRegisterResponseDto getAllCommentList(){
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

    public List<Comment> getComments(Long commentId, Pageable page) {
        return commentId == null ?
                this.commentRepository.findAllByOrderByIdDesc(page) :
                this.commentRepository.findByIdLessThanOrderByIdDesc(commentId, page);
    }

    public void deleteAll(Member member){
        List<Comment> commentList = commentRepository.findByMember(member);
        commentRepository.deleteAll(commentList);
    }

    public Boolean hasNext(Long id) {
        if (id == null) return false;
        return this.commentRepository.existsByIdLessThan(id);
    }
}
