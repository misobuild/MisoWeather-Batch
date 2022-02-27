package com.misoweather.misoweatherservice.mapping;

import com.misoweather.misoweatherservice.comment.service.CommentService;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.member.dto.DeleteMemberRequestDto;
import com.misoweather.misoweatherservice.member.dto.LoginRequestDto;
import com.misoweather.misoweatherservice.member.dto.MemberInfoResponseDto;
import com.misoweather.misoweatherservice.member.dto.SignUpRequestDto;
import com.misoweather.misoweatherservice.member.service.MemberService;
import com.misoweather.misoweatherservice.region.service.RegionService;
import com.misoweather.misoweatherservice.survey.service.SurveyService;
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
}
