package com.misoweather.misoweatherservice.comment.service;

import com.misoweather.misoweatherservice.global.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.comment.CommentRepository;
import com.misoweather.misoweatherservice.global.reader.ContentReader;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

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
    @DisplayName("성공 테스트: <PageRequest>와 크기에 맞는 <Comment>리스트를 반환한다.")
    void getComments() {
        // given
        Comment givenComment = Comment.builder()
                .content(contentReader.check("안녕하세요"))
                .bigScale(BigScaleEnum.getEnum("서울특별시").toString())
                .deleted(Boolean.FALSE)
                .build();

        given(commentRepository.findByIdLessThanOrderByIdDesc(anyLong(), any(Pageable.class))).willReturn(List.of(givenComment));

        // when
        List<Comment> commentListExist = commentService.getComments(1L, PageRequest.of(0,1));

        // then
        assertThat(commentListExist.get(0), is(givenComment));
    }

    @Test
    @DisplayName("분기 성공 테스트: <PageRequest>와 크기에 맞는 <Comment>리스트가 empty 리스트이다.")
    void getCommentsWhenListNull() {
        // given
        given(commentRepository.findAllByOrderByIdDesc(any(Pageable.class))).willReturn(List.of());

        // when
        List<Comment> commentListNull = commentService.getComments(null, PageRequest.of(0,1));

        // then
        assertThat(commentListNull, is(List.of()));
    }

    @Test
    @DisplayName("분기 성공 테스트: <Comment>의 다음 코멘트 있을 때 Boolean.TRUE 반환한다.")
    void hasNextTestHasNext(){
        // given
        given(commentRepository.existsByIdLessThan(0L)).willReturn(Boolean.TRUE);

        // when
        Boolean secondResult = commentService.hasNext(0L);

        // then
        assertThat(secondResult, is(Boolean.TRUE));
    }

    @Test
    @DisplayName("분기 성공 테스트: <Comment>가 null 일 때 Boolean.FALSE 반환한다.")
    void hasNextTestNothing(){
        // given, when
        Boolean thirdResult = commentService.hasNext(null);

        // then
        assertThat(thirdResult, is(Boolean.FALSE));
    }

    @Test
    @DisplayName("성공 테스트: <Comment> givenComment의 id 값을 반환한다.")
    void getLastId(){
        // given
        Comment givenComment = spy(Comment.class);
        doReturn(11L).when(givenComment).getId();

        // when
        Long caseOne = commentService.getLastId(List.of());
        Long caseTwo = commentService.getLastId(List.of(givenComment));

        // then
        assertThat(caseOne, is(nullValue()));
        assertThat(caseTwo, is(11L));
    }
}
