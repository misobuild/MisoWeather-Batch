package com.misoweather.misoweatherservice.comment.service;

import com.misoweather.misoweatherservice.comment.dto.CommentRegisterResponseDto;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.comment.CommentRepository;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.global.reader.ContentReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("CommentService에서 비즈니스 로직 없는 JPA 활용 부분을 테스트한다.")
public class CommentServiceJpaTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EntityManager entityManager;

    private ContentReader contentReader;
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        this.contentReader = new ContentReader();
        this.commentService = new CommentService(this.commentRepository, this.contentReader);
    }

    @Test
    @DisplayName("<Comment> 빌드하여 저장소에 save() 한다.")
    void saveComment(){
        // given
        Member givenMember = entityManager.find(Member.class, 304L);
        String givenContent = "안녕하세요";
        String givenBigScale = "서울특별시";

        // when
        Comment actual = commentService.saveComment(givenContent, givenMember, givenBigScale);

        // then
        assertThat(actual.getMember(), is(givenMember));
        assertThat(actual.getBigScale(), is("서울"));
        assertThat(actual.getContent(), is(givenContent));
    }

    @Test
    @DisplayName("<Comment> 리스트 찾아 반환한다.")
    void getAllCommentList(){
        // given
        Comment foundComment = commentRepository.findAllByOrderByIdDesc(Pageable.ofSize(1)).get(0);

        // when
        CommentRegisterResponseDto actual = commentService.getAllCommentList();

        // then
        assertThat(actual.getCommentList().get(actual.getCommentList().size() - 1), is(foundComment));
    }
}
