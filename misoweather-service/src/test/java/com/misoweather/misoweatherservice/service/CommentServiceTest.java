package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.comment.CommentRepository;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.dto.response.comment.CommentRegisterResponseDto;
import com.misoweather.misoweatherservice.utils.reader.ContentReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

//TODO assertThat 으로 교체
@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService 테스트")
public class CommentServiceTest {

    @Mock private CommentRepository commentRepository;
    @InjectMocks private CommentService commentService;
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

        Comment savedComment = commentService.saveComment("안녕하세요", givenMember, "서울특별시");

        assertThat(savedComment, is(givenComment));
    }

    @Test
    @DisplayName("getAllCommentList() 테스트")
    void getAllComment(){
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

        given(commentRepository.findAll()).willReturn(List.of(givenComment));

        CommentRegisterResponseDto resultDto = commentService.getAllCommentList();

        assertThat(resultDto.getCommentList().get(0), is(givenComment));
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

        assertThat(commentListNull, is(nullValue()));
        assertThat(commentListExist.get(0), is(givenComment));

        verify(commentRepository, times(1)).findAllByOrderByIdDesc(any(Pageable.class));
        verify(commentRepository, times(1)).findByIdLessThanOrderByIdDesc(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("hasNext() 테스트")
    void hasNextTest(){
        Long givenFirstId = 0L;
        Long givenSecondId = 1L;

        given(commentRepository.existsByIdLessThan(0L)).willReturn(Boolean.FALSE);
        given(commentRepository.existsByIdLessThan(1L)).willReturn(Boolean.TRUE);

        Boolean firstResult = commentService.hasNext(givenFirstId);
        Boolean secondResult = commentService.hasNext(givenSecondId);
        Boolean thirdResult = commentService.hasNext(null);

        Assertions.assertEquals(firstResult, Boolean.FALSE);
        Assertions.assertEquals(secondResult, Boolean.TRUE);
        Assertions.assertEquals(thirdResult, Boolean.FALSE);

        assertThat(firstResult, is(Boolean.FALSE));
        assertThat(secondResult, is(Boolean.TRUE));
        assertThat(thirdResult, is(Boolean.FALSE));

    }
}
