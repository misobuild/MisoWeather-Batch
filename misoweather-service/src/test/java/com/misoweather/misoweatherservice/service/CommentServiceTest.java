package com.misoweather.misoweatherservice.service;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;

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
//        this.commentService = new CommentService(commentRepository,memberRegionMappingRepository, contentReader);

    }

    @Test
    @DisplayName("CommentService: registerComment() 테스트")
    void registerCommentTest(){
        String bigScale = "서울특별시";

        Member member = Member.builder()
                .socialId("12345")
                .emoji("a")
                .nickname("행복한 가짜광대")
                .socialType("kakao")
                .build();

        MemberRegionMapping memberRegionMapping = MemberRegionMapping.builder()
                .regionStatus(RegionEnum.DEFAULT)
                .member(member)
                .region(null)
                .build();

        Comment comment = Comment.builder()
                .content("We're happy in Misoweather")
                .bigScale(bigScale)
                .member(member)
                .nickname(member.getNickname())
                .deleted(Boolean.FALSE)
                .emoji(member.getEmoji())
                .build();

        List memberRegionMappingList = new ArrayList();
        memberRegionMappingList.add(memberRegionMapping);
        List bigScaleList = new ArrayList();
        bigScaleList.add(memberRegionMapping);
        List commentLIst = new ArrayList();
        commentLIst.add(comment);

        given(memberRegionMappingRepository.findMemberRegionMappingByMember(member).stream()
        )
                .willReturn(Stream.of(memberRegionMappingList));


        given(commentRepository.findAll())
                .willReturn(commentLIst);

        // when
//        final CommentRegisterResponseDto result = commentService
//                .registerComment(CommentRegisterRequestDtoBuilder.build("행복한 가짜광대"), member);

        // then
//        Assertions.assertEquals(commentList.getCommentList().get(0).getContent(), "We're happy in Misoweather");
    }

}
