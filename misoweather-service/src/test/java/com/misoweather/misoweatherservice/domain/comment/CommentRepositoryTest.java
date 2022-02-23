package com.misoweather.misoweatherservice.domain;

import com.misoweather.misoweatherservice.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.comment.CommentRepository;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member.MemberRepository;
import com.misoweather.misoweatherservice.utils.reader.ContentReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired CommentRepository commentRepository;
    private ContentReader contentReader;

    @BeforeEach
    void setUp(){
        this.contentReader = new ContentReader();
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
}
