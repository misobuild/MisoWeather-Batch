package com.misoweather.misoweatherservice.mapping;

import com.misoweather.misoweatherservice.comment.service.CommentService;
import com.misoweather.misoweatherservice.domain.comment.Comment;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMapping;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.domain.survey.Answer;
import com.misoweather.misoweatherservice.domain.survey.Survey;
import com.misoweather.misoweatherservice.comment.dto.CommentRegisterRequestDto;
import com.misoweather.misoweatherservice.global.api.ListDto;
import com.misoweather.misoweatherservice.member.dto.DeleteMemberRequestDto;
import com.misoweather.misoweatherservice.member.dto.LoginRequestDto;
import com.misoweather.misoweatherservice.member.dto.SignUpRequestDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyDto;
import com.misoweather.misoweatherservice.comment.dto.CommentListResponseDto;
import com.misoweather.misoweatherservice.comment.dto.CommentRegisterResponseDto;
import com.misoweather.misoweatherservice.member.dto.MemberInfoResponseDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerStatusDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyResponseDto;
import com.misoweather.misoweatherservice.global.utils.reader.SurveyReader;
import com.misoweather.misoweatherservice.member.service.MemberService;
import com.misoweather.misoweatherservice.region.RegionService;
import com.misoweather.misoweatherservice.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplexFlowService {
    private static final Long recentDays = 7L;

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

    public CommentListResponseDto getCommentList(Long commentId, Pageable page){
        List<Comment> rawCommentList = commentService.getComments(commentId, page);
        Long lasIdOfList = commentService.getLastId(rawCommentList);
        return new CommentListResponseDto(rawCommentList, commentService.hasNext(lasIdOfList));
    }

    // RegionService
    @Transactional
    public MemberRegionMapping updateRegion(Member member, Long regionId){
        Region targetRegion = regionService.getRegion(regionId);
        List<MemberRegionMapping> memberRegionMappingList = mappingService.getMemberRegionMappingList(member);
        return regionService.updateRegion(memberRegionMappingList, targetRegion);
    }

    // SurveyService
    public AnswerSurveyResponseDto answerSurvey(Member member, AnswerSurveyDto answerSurveyDto){
        Answer answer = surveyService.getAnswer(answerSurveyDto);
        Survey survey = surveyService.getSurvey(answerSurveyDto);
        surveyService.checkAnswerAndSurvey(answer, survey);
        List<MemberSurveyMapping> memberSurveyMappingList = mappingService.filterMemberSurveyMappingList(member, survey);
        surveyService.checkMemberSurveyMappingList(memberSurveyMappingList);
        MemberSurveyMapping memberSurveyMapping = surveyService.buildMemberSurveyMapping(member, answer, survey, answerSurveyDto);
        mappingService.saveMemberSurveyMapping(memberSurveyMapping);
        return surveyService.buildAnswerSurveyResponseDto(answer, survey);
    }

    public ListDto<AnswerStatusDto> getAnswerStatus(Member member){
        List<Long> surveyIdList = surveyService.getAllSurveyId();
        List<AnswerStatusDto> answerStatusDtoList = mappingService.buildFromFilteredMemberSurveyMappingList(member, surveyIdList);
        List<AnswerStatusDto> nullStatusDtoList = surveyService.buildAnswerStatusNullDtoList(surveyIdList);

        answerStatusDtoList.addAll(nullStatusDtoList);
        answerStatusDtoList.sort(Comparator.comparing(AnswerStatusDto::getSurveyId));

        return surveyService.buildAnswerStatusResponseDtoList(answerStatusDtoList);
    }

    public ListDto<SurveyReader> getSurveyResultList(String shortBigScale){
        List<MemberSurveyMapping> recentSurveyList = mappingService.getRecentSurveyListFor(recentDays);
        List<MemberSurveyMapping> recentIdMatchSurveyList = surveyService.getSurveyMatchesBigScaleList(recentSurveyList, shortBigScale);
        List<SurveyReader> surveyReaderList = surveyService.getSurveyReaderMatchesIdList(recentIdMatchSurveyList);

        return surveyService.setSurveyReaderList(surveyReaderList);
    }
}
