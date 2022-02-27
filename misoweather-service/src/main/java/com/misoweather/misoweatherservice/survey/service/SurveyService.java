package com.misoweather.misoweatherservice.survey.service;

import com.misoweather.misoweatherservice.global.api.ListDto;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMapping;
import com.misoweather.misoweatherservice.domain.survey.Answer;
import com.misoweather.misoweatherservice.domain.survey.AnswerRepository;
import com.misoweather.misoweatherservice.domain.survey.Survey;
import com.misoweather.misoweatherservice.domain.survey.SurveyRepository;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerStatusDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyJoinDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyResponseDto;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import com.misoweather.misoweatherservice.mapping.reader.SurveyReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final AnswerRepository answerRepository;

    public ListDto<AnswerSurveyJoinDto> getAnswerList(Long surveyId) {
        List<AnswerSurveyJoinDto> answerSurveyJoinList = answerRepository.findAnswerSurveyJoinBySurveyId(surveyId);
        if (answerSurveyJoinList.isEmpty()) throw new ApiCustomException(HttpStatusEnum.NOT_FOUND);
        return ListDto.<AnswerSurveyJoinDto>builder().responseList(answerSurveyJoinList).build();
    }

    public List<Long> getAllSurveyId() {
        return surveyRepository.findAll().stream()
                .map(item -> item.getId())
                .collect(Collectors.toList());
    }

    public List<AnswerStatusDto> buildAnswerStatusNullDtoList(List<Long> surveyIdList) {
        return surveyIdList.stream()
                // TODO 생성자 하나 더 만들어서 다를 경우 수행하는 게 달라지게 한다.
                .map(item -> AnswerStatusDto.builder()
                        .surveyId(item)
                        .memberAnswer(null)
                        .answered(Boolean.FALSE).build())
                .collect(Collectors.toList());
    }

    public ListDto<AnswerStatusDto> buildAnswerStatusResponseDtoList(List<AnswerStatusDto> answerStatusDtoList){
        return ListDto.<AnswerStatusDto>builder().responseList(answerStatusDtoList).build();
    }

    public Answer getAnswer(AnswerSurveyDto answerSurveyDto) {
        return answerRepository.findById(answerSurveyDto.getAnswerId())
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
    }


    public Survey getSurvey(AnswerSurveyDto answerSurveyDto) {
        return surveyRepository.findById(answerSurveyDto.getSurveyId())
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
    }

    public void checkAnswerAndSurvey(Answer answer, Survey survey) {
        if (!answer.getSurvey().getId().equals(survey.getId())) {
            throw new ApiCustomException(HttpStatusEnum.CONFLICT);
        }
    }

    public void checkMemberSurveyMappingList(List<MemberSurveyMapping> memberSurveyMappingList) {
        if (!memberSurveyMappingList.isEmpty()) throw new ApiCustomException(HttpStatusEnum.CONFLICT);
    }

    public MemberSurveyMapping buildMemberSurveyMapping(Member member, Answer answer, Survey survey, AnswerSurveyDto answerSurveyDto) {
        return MemberSurveyMapping.builder()
                .member(member).answer(answer).survey(survey)
                .shortBigScale(answerSurveyDto.getShortBigScale())
                .build();
    }

    public AnswerSurveyResponseDto buildAnswerSurveyResponseDto(Answer answer, Survey survey) {
        return AnswerSurveyResponseDto.builder()
                .surveyDescription(survey.getDescription())
                .answer(answer.getContent())
                .build();
    }

    public List<MemberSurveyMapping> getSurveyMatchesBigScaleList(List<MemberSurveyMapping> recentSurveyList, String shortBigScale){
        List<MemberSurveyMapping> recentIdMatchSurveyList = recentSurveyList;
        if (shortBigScale != null) {
            recentIdMatchSurveyList = recentSurveyList.stream()
                    .filter(item -> item.getShortBigScale().equals(shortBigScale)).collect(Collectors.toList());
        }
        return recentIdMatchSurveyList;
    }

    public List<SurveyReader> getSurveyReaderMatchesIdList(List<MemberSurveyMapping> recentIdMatchSurveyList){
        List<SurveyReader> surveyReaderList = new ArrayList<>();
        List<Survey> totalSurveyList = surveyRepository.findAll();

        for (Survey survey : totalSurveyList) {
            List<MemberSurveyMapping> conditionedSurveyList = recentIdMatchSurveyList.stream()
                    .filter(item -> item.getSurvey().getId().equals(survey.getId()))
                    .collect(Collectors.toList());
            surveyReaderList.add(SurveyReader.builder()
                    .surveyId(survey.getId())
                    .surveyTitle(survey.getTitle())
                    .surveyDescription(survey.getDescription())
                    .msmList(conditionedSurveyList).build());
        }

        return surveyReaderList;
    }

    public ListDto<SurveyReader> setSurveyReaderList(List<SurveyReader> surveyReaderList){
        surveyReaderList.forEach(SurveyReader::setInfoMap);
        surveyReaderList.forEach(SurveyReader::setValues);

        return ListDto.<SurveyReader>builder()
                .responseList(surveyReaderList)
                .build();
    }
}