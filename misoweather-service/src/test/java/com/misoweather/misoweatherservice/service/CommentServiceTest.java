package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.comment.CommentRepository;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.utils.reader.ContentReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService 테스트")
public class CommentServiceTest {

    @Mock private CommentRepository commentRepository;
    private CommentService commentService;
    private ContentReader contentReader;

    @BeforeEach
    void setUp(){
        this.contentReader = new ContentReader();
        this.commentService = new CommentService(commentRepository, contentReader);
    }

    @Test
    @DisplayName("saveComment() 테스트")
    void saveComment(){
        Member givenMember = Member.builder()
                .socialId("12345")
                .emoji("a")
                .nickname("행복한 가짜광대")
                .socialType("kakao")
                .build();

        Comment givenComment = Comment.builder()
                .content(contentReader.checker("안녕하세요"))
                .bigScale(BigScaleEnum.getEnum("서울특별시").toString())
                .member(givenMember)
                .nickname(givenMember.getNickname())
                .deleted(Boolean.FALSE)
                .emoji(givenMember.getEmoji())
                .build();

        given(commentRepository.save(any(Comment.class))).willReturn(givenComment);

        Comment savedComment = commentRepository.save(givenComment);

        Assertions.assertEquals(savedComment.getContent(), "안녕하세요");
        Assertions.assertEquals(savedComment.getBigScale(), "서울");
        Assertions.assertEquals(savedComment.getMember().getSocialId(), "12345");
        Assertions.assertEquals(savedComment.getEmoji(), "a");
        Assertions.assertEquals(savedComment.getMember().getNickname(), "행복한 가짜광대");
        Assertions.assertEquals(savedComment.getDeleted(), Boolean.FALSE);

        verify(commentRepository, times(1)).save(givenComment);
    }

    @Test
    @DisplayName("getComments() 테스트")
    void getComments() {
        Member givenMember = Member.builder()
                .socialId("67890")
                .emoji("b")
                .nickname("우울한 진짜광대")
                .socialType("apple")
                .build();

        Comment givenComment = Comment.builder()
                .content(contentReader.checker("안녕하세요"))
                .bigScale(BigScaleEnum.getEnum("서울특별시").toString())
                .member(givenMember)
                .nickname(givenMember.getNickname())
                .deleted(Boolean.FALSE)
                .emoji(givenMember.getEmoji())
                .build();

        given(commentRepository.findAllByOrderByIdDesc(any(Pageable.class))).willReturn(List.of());
        given(commentRepository.findByIdLessThanOrderByIdDesc(anyLong(), any(Pageable.class))).willReturn(List.of(givenComment));

        List<Comment> commentListExist = commentService.getComments(1L, PageRequest.of(0,1));
        List<Comment> commentListNull = commentService.getComments(null, PageRequest.of(0,1));

        Assertions.assertEquals(commentListExist.get(0).getMember().getSocialId(), "67890");
        Assertions.assertEquals(commentListExist.get(0).getContent(), "안녕하세요");
        Assertions.assertEquals(commentListExist.get(0).getBigScale(), "서울");
        Assertions.assertEquals(commentListNull, List.of());

        verify(commentRepository, times(1)).findAllByOrderByIdDesc(any(Pageable.class));
        verify(commentRepository, times(1)).findByIdLessThanOrderByIdDesc(anyLong(), any(Pageable.class));
    }
}
