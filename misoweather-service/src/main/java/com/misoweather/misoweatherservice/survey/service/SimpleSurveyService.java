package com.misoweather.misoweatherservice.survey.service;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMapping;
import com.misoweather.misoweatherservice.domain.survey.Answer;
import com.misoweather.misoweatherservice.domain.survey.Survey;
import com.misoweather.misoweatherservice.global.api.ListDto;
import com.misoweather.misoweatherservice.global.utils.reader.SurveyReader;
import com.misoweather.misoweatherservice.mapping.MappingService;
import com.misoweather.misoweatherservice.survey.dto.AnswerStatusDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleSurveyService {
    private static final Long recentDays = 7L;

    private final SurveyService surveyService;
    private final MappingService mappingService;

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
