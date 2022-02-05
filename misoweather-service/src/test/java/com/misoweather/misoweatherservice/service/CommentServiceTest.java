package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.constants.RegionEnum;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.comment.CommentRepository;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.region.RegionRepository;
import com.misoweather.misoweatherservice.dto.request.comment.CommentRegisterRequestDto;
import com.misoweather.misoweatherservice.dto.request.comment.CommentRegisterRequestDtoBuilder;
import com.misoweather.misoweatherservice.dto.response.comment.CommentRegisterResponseDto;
import com.misoweather.misoweatherservice.exception.ApiCustomException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService 테스트")
public class CommentServiceTest {

    @Mock private CommentRepository commentRepository;
    @Mock private MemberRegionMappingRepository memberRegionMappingRepository;
    @Mock private RegionRepository regionRepository;
    private CommentService commentService;
    private ContentReader contentReader;
    private CommentRegisterRequestDto commentRegisterRequestDto;

    @BeforeEach
    void setUp(){
        this.contentReader = new ContentReader();
    }

    @Test
    @DisplayName("saveComment() 테스트")
    void saveComment(){
        String content = "안녕하세요";
        String bigScale = "서울특별시";

        Member member = Member.builder()
                .socialId("12345")
                .emoji("a")
                .nickname("행복한 가짜광대")
                .socialType("kakao")
                .build();

        Comment comment = Comment.builder()
                .content(contentReader.checker(content))
                .bigScale(BigScaleEnum.getEnum(bigScale).toString())
                .member(member)
                .nickname(member.getNickname())
                .deleted(Boolean.FALSE)
                .emoji(member.getEmoji())
                .build();

        given(commentRepository.save(comment)).willReturn(comment);

        Comment savedComment = commentRepository.save(comment);

        Assertions.assertEquals(savedComment.getContent(), "안녕하세요");
        Assertions.assertEquals(savedComment.getBigScale(), "서울");
        Assertions.assertEquals(savedComment.getMember().getSocialId(), "12345");
        Assertions.assertEquals(savedComment.getEmoji(), "a");
        Assertions.assertEquals(savedComment.getMember().getNickname(), "행복한 가짜광대");
        Assertions.assertEquals(savedComment.getDeleted(), Boolean.FALSE);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    @DisplayName("getComments() 테스트")
    void getComments() {
        Member member = Member.builder()
                .socialId("67890")
                .emoji("b")
                .nickname("우울한 진짜광대")
                .socialType("apple")
                .build();

        Member member2 = Member.builder()
                .socialId("12345")
                .emoji("a")
                .nickname("행복한 가짜광대")
                .socialType("kakao")
                .build();

        Comment comment = Comment.builder()
                .content(contentReader.checker("안녕하세요"))
                .bigScale(BigScaleEnum.getEnum("서울특별시").toString())
                .member(member)
                .nickname(member.getNickname())
                .deleted(Boolean.FALSE)
                .emoji(member.getEmoji())
                .build();

        Comment comment2 = Comment.builder()
                .content(contentReader.checker("안녕히계세요"))
                .bigScale(BigScaleEnum.getEnum("부산광역시").toString())
                .member(member2)
                .nickname(member2.getNickname())
                .deleted(Boolean.FALSE)
                .emoji(member2.getEmoji())
                .build();


        given(commentRepository.findAllByOrderByIdDesc(any(Pageable.class))).willReturn(List.of(comment2));
        given(commentRepository.findByIdLessThanOrderByIdDesc(anyLong(), any(Pageable.class))).willReturn(List.of(comment));

        List<Comment> resultCommentList = commentService.getComments(1L, PageRequest.of(0,1));
        List<Comment> resultCommentList2 = commentService.getComments(null, PageRequest.of(0,1));

        Assertions.assertEquals(resultCommentList2.get(0).getMember().getMemberId(), "67890");
        Assertions.assertEquals(resultCommentList.get(0).getMember().getMemberId(), "12345");

        verify(commentRepository, times(1)).findAllByOrderByIdDesc(any(Pageable.class));
        verify(commentRepository, times(1)).findByIdLessThanOrderByIdDesc(anyLong(), any(Pageable.class));
    }
}
