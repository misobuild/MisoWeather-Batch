package com.misoweather.misoweatherservice.domain.comment;

import com.misoweather.misoweatherservice.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member.MemberRepository;
import com.misoweather.misoweatherservice.service.CommentService;
import com.misoweather.misoweatherservice.utils.reader.ContentReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired CommentRepository commentRepository;
    private CommentService commentService;
    private ContentReader contentReader;

    @BeforeEach
    void setUp(){
        this.contentReader = new ContentReader();
        this.commentService = new CommentService(commentRepository, contentReader);
    }

    @Test
    @DisplayName("CommentRepository: save()")
    void save(){
        // given
        Member givenMember = Member.builder()
                .socialId("12345")
                .emoji("a")
                .nickname("행복한 가짜광대")
                .socialType("kakao")
                .build();

        memberRepository.save(givenMember);

        Comment givenComment = Comment.builder()
                .content(contentReader.checker("안녕하세요"))
                .bigScale(BigScaleEnum.getEnum("서울특별시").toString())
                .member(givenMember)
                .nickname(givenMember.getNickname())
                .deleted(Boolean.FALSE)
                .emoji(givenMember.getEmoji())
                .build();

        // when
        Comment savedComment = commentRepository.save(givenComment);

        // then
        assertThat(savedComment.getMember(), is(givenMember));
        assertThat(savedComment, is(givenComment));
    }

    @Test
    @DisplayName("CommentRepository: findByMember() 아이디로 멤버를 찾는다")
    void findByMember(){
        // given
        Member givenMember = Member.builder()
                .socialId("12345")
                .emoji("a")
                .nickname("행복한 가짜광대")
                .socialType("kakao")
                .build();

        memberRepository.save(givenMember);

        Comment givenComment = Comment.builder()
                .content(contentReader.checker("안녕하세요"))
                .bigScale(BigScaleEnum.getEnum("서울특별시").toString())
                .member(givenMember)
                .nickname(givenMember.getNickname())
                .deleted(Boolean.FALSE)
                .emoji(givenMember.getEmoji())
                .build();

        // when
        Comment savedComment = commentRepository.save(givenComment);
        List<Comment> foundCommentList = commentRepository.findByMember(givenMember);

        // then
        assertThat(foundCommentList.get(0), is(savedComment));
    }


    @Test
    @DisplayName("CommentRepository: givenMember의 코멘트 모두 지운다")
    void deleteAll(){
        // given
        Member givenMember = Member.builder()
                .socialId("12345")
                .emoji("a")
                .nickname("행복한 가짜광대")
                .socialType("kakao")
                .build();

        memberRepository.save(givenMember);

        Comment givenComment = Comment.builder()
                .content(contentReader.checker("안녕하세요"))
                .bigScale(BigScaleEnum.getEnum("서울특별시").toString())
                .member(givenMember)
                .nickname(givenMember.getNickname())
                .deleted(Boolean.FALSE)
                .emoji(givenMember.getEmoji())
                .build();

        // when
        commentRepository.save(givenComment);
        commentService.deleteAll(givenMember);

        // then
        assertThat(commentRepository.findByMember(givenMember), iterableWithSize(0));
    }



}
