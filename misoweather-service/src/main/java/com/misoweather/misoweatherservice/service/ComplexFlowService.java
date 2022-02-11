package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.dto.request.comment.CommentRegisterRequestDto;
import com.misoweather.misoweatherservice.dto.request.member.DeleteMemberRequestDto;
import com.misoweather.misoweatherservice.dto.request.member.LoginRequestDto;
import com.misoweather.misoweatherservice.dto.request.member.SignUpRequestDto;
import com.misoweather.misoweatherservice.dto.response.comment.CommentRegisterResponseDto;
import com.misoweather.misoweatherservice.dto.response.member.MemberInfoResponseDto;
import com.misoweather.misoweatherservice.exception.ApiCustomException;
import com.misoweather.misoweatherservice.utils.factory.ValidatorFactory;
import com.misoweather.misoweatherservice.utils.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplexFlowService {

    private final MemberService memberService;
    private final MappingService mappingService;
    private final CommentService commentService;
    private final RegionService regionService;
    private final SurveyService surveyService;

    // MemberService
    @Transactional
    public Member registerMember(SignUpRequestDto signUpRequestDto, String socialToken){
        memberService.checkToken(signUpRequestDto.getSocialId(), signUpRequestDto.getSocialType(), socialToken);
        memberService.checkExistence(signUpRequestDto.getSocialId(), signUpRequestDto.getSocialType(), signUpRequestDto.getNickname());
        Member registeredMember = memberService.buildMemberAndSave(signUpRequestDto);
        Region defaultRegion = regionService.getRegion(signUpRequestDto.getDefaultRegionId());
        mappingService.buildMemberRegionMappingAndSave(registeredMember, defaultRegion);
        return registeredMember;
    }

    public void deleteMember(DeleteMemberRequestDto deleteMemberRequestDto){
        Member member = memberService.getMember(deleteMemberRequestDto.getSocialId(), deleteMemberRequestDto.getSocialType());
        memberService.deleteMember(member);
        mappingService.deleteMemberSurvey(member);
        mappingService.deleteMemberRegion(member);
        commentService.deleteAll(member);
    }

    public MemberInfoResponseDto getMemberInfo(Member member){
        List<MemberRegionMapping> memberRegionMappingList = mappingService.getMemberRegionMappingList(member);
        MemberRegionMapping memberRegionMapping = mappingService.filterMemberRegionMappingList(memberRegionMappingList);
        return memberService.buildMemberInfoResponse(member, memberRegionMapping);
    }

    public String reissue(LoginRequestDto loginRequestDto, String socialToken){
        memberService.checkToken(loginRequestDto.getSocialId(), loginRequestDto.getSocialType(), socialToken);
        Member member = memberService.getMember(loginRequestDto.getSocialId(), loginRequestDto.getSocialType());
        return memberService.createToken(member);
    }

    // CommentService
    public CommentRegisterResponseDto registerComment(CommentRegisterRequestDto commentRegisterRequestDto, Member member){
        String bigScale = mappingService.getBigScale(member);
        commentService.saveComment(commentRegisterRequestDto.getContent(), member, bigScale);
        return commentService.getAllCommentList();
    }

    // RegionService
    @Transactional
    public MemberRegionMapping updateRegion(Member member, Long regionId){
        Region targetRegion = regionService.getRegion(regionId);
        List<MemberRegionMapping> memberRegionMappingList = mappingService.getMemberRegionMappingList(member);
        return regionService.updateRegion(memberRegionMappingList, targetRegion);
    }
}
