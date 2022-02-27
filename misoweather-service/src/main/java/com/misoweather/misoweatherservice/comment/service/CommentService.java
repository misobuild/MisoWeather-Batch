package com.misoweather.misoweatherservice.comment.service;

import com.misoweather.misoweatherservice.global.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.comment.CommentRepository;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.comment.dto.CommentRegisterResponseDto;
import com.misoweather.misoweatherservice.global.utils.reader.ContentReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ContentReader contentReader;

    public Comment saveComment(String content, Member member, String bigScale){
        Comment comment = Comment.builder()
                .content(contentReader.checker(content))
                .bigScale(BigScaleEnum.getEnum(bigScale).toString())
                .member(member)
                .nickname(member.getNickname())
                .deleted(Boolean.FALSE)
                .emoji(member.getEmoji())
                .build();
        return commentRepository.save(comment);
    }

    public CommentRegisterResponseDto getAllCommentList(){
        return CommentRegisterResponseDto.builder()
                .commentList(commentRepository.findAll())
                .build();
    }

    public List<Comment> getComments(Long commentId, Pageable page) {
        return commentId == null ?
                this.commentRepository.findAllByOrderByIdDesc(page) :
                this.commentRepository.findByIdLessThanOrderByIdDesc(commentId, page);
    }

    public Long getLastId(List<Comment> rawCommentList){
        return  rawCommentList.isEmpty() ? null : rawCommentList.get(rawCommentList.size() - 1).getId();
    }

    public void deleteAll(Member member){
        List<Comment> commentList = commentRepository.findByMember(member);
        commentRepository.deleteAllInBatch(commentList);
    }

    public Boolean hasNext(Long id) {
        if (id == null) return false;
        return this.commentRepository.existsByIdLessThan(id);
    }
}
