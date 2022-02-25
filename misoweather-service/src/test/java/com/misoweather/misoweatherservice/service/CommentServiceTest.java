package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.comment.CommentRepository;
import com.misoweather.misoweatherservice.utils.reader.ContentReader;
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
    @DisplayName("getComments() 테스트")
    void getComments() {
        Comment givenComment = Comment.builder()
                .content(contentReader.checker("안녕하세요"))
                .bigScale(BigScaleEnum.getEnum("서울특별시").toString())
                .deleted(Boolean.FALSE)
                .build();

        given(commentRepository.findByIdLessThanOrderByIdDesc(anyLong(), any(Pageable.class))).willReturn(List.of(givenComment));

        List<Comment> commentListExist = commentService.getComments(1L, PageRequest.of(0,1));

        assertThat(commentListExist.get(0), is(givenComment));
    }

    @Test
    @DisplayName("getComments() when commentListNull 테스트")
    void getCommentsWhenListNull() {
        given(commentRepository.findAllByOrderByIdDesc(any(Pageable.class))).willReturn(List.of());

        List<Comment> commentListNull = commentService.getComments(null, PageRequest.of(0,1));

        assertThat(commentListNull, is(List.of()));
    }

    @Test
    @DisplayName("hasNext() 테스트")
    void hasNextTest(){
        given(commentRepository.existsByIdLessThan(0L)).willReturn(Boolean.FALSE);
        given(commentRepository.existsByIdLessThan(1L)).willReturn(Boolean.TRUE);

        Boolean firstResult = commentService.hasNext(0L);
        Boolean secondResult = commentService.hasNext(1L);
        Boolean thirdResult = commentService.hasNext(null);

        assertThat(firstResult, is(Boolean.FALSE));
        assertThat(secondResult, is(Boolean.TRUE));
        assertThat(thirdResult, is(Boolean.FALSE));
    }

    @Test
    @DisplayName("getLastId()")
    void getLastId(){
        Comment givenComment = spy(Comment.class);
        doReturn(11L).when(givenComment).getId();
        List<Comment> caseOneList = List.of();
        List<Comment> caseTwoList = List.of(givenComment);

        assertThat(commentService.getLastId(caseOneList), is(nullValue()));
        assertThat(commentService.getLastId(caseTwoList), is(11L));
    }
}
