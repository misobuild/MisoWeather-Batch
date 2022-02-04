package com.misoweather.misoweatherservice.dto.response.comment;

import com.misoweather.misoweatherservice.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommentListResponseDto {
    private List<Comment> commentList;
    private Boolean hasNext;

    @Builder
    public CommentListResponseDto(List<Comment> commentList, Boolean hasNext) {
        this.commentList = commentList;
        this.hasNext = hasNext;
    }
}
