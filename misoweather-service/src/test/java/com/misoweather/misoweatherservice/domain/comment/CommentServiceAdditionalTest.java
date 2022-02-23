package com.misoweather.misoweatherservice.domain.comment;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.service.CommentService;
import com.misoweather.misoweatherservice.utils.reader.ContentReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

//TODO assertThat 으로 교체
@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService 테스트")
public class CommentServiceAdditionalTest {

    @Mock private CommentRepository commentRepository;
    @InjectMocks private CommentService commentService;
    private ContentReader contentReader;

    @BeforeEach
    void setUp(){
        this.contentReader = new ContentReader();
        this.commentService = new CommentService(commentRepository, contentReader);
    }

    @Test
    @DisplayName("getLastId()")
    void getLastId(){
        // given
        Member givenMember = Member.builder()
                .socialId("67890")
                .emoji("b")
                .nickname("우울한 진짜광대")
                .socialType("apple")
                .build();

        Comment givenComment = new Comment(11L, "안녕하세요", "서울"
                , givenMember.getNickname(),givenMember, Boolean.FALSE, givenMember.getEmoji());

        List<Comment> caseOneList = List.of();
        List<Comment> caseTwoList = List.of(givenComment);

        // when
        assertThat(commentService.getLastId(caseOneList), is(nullValue()));
        assertThat(commentService.getLastId(caseTwoList), is(11L));
    }
}
