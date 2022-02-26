package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.constants.RegionEnum;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMapping;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMappingRepository;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.domain.survey.Survey;
import com.misoweather.misoweatherservice.dto.response.survey.AnswerStatusDto;
import com.misoweather.misoweatherservice.exception.ApiCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MappingService {
    private final MemberRegionMappingRepository memberRegionMappingRepository;
    private final MemberSurveyMappingRepository memberSurveyMappingRepository;

    public List<MemberRegionMapping> getMemberRegionMappingList(Member member) {
        return memberRegionMappingRepository.findMemberRegionMappingByMember(member);
    }

    public void deleteMemberRegion(Member member) {
        List<MemberRegionMapping> memberRegionMappingList = memberRegionMappingRepository.findMemberRegionMappingByMember(member);
        memberRegionMappingRepository.deleteAll(memberRegionMappingList);
    }

    public void saveMemberSurveyMapping(MemberSurveyMapping memberSurveyMapping) {
        memberSurveyMappingRepository.save(memberSurveyMapping);
    }

    public void deleteMemberSurvey(Member member) {
        List<MemberSurveyMapping> memberSurveyMappingList = memberSurveyMappingRepository.findByMember(member);
        memberSurveyMappingRepository.deleteAll(memberSurveyMappingList);
    }

    public List<MemberSurveyMapping> getRecentSurveyListFor(Long days) {
        return memberSurveyMappingRepository.findByCreatedAtAfter(LocalDateTime
                .of(LocalDate.now().minusDays(days), LocalTime.of(23, 59)));
    }

    public String getBigScale(Member member) {
        return memberRegionMappingRepository.findMemberRegionMappingByMember(member).stream()
                .filter(item -> item.getRegionStatus().equals(RegionEnum.DEFAULT))
                .map(item -> item.getRegion().getBigScale())
                .findFirst()
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
    }

    public List<MemberSurveyMapping> filterMemberSurveyMappingList(Member member, Survey survey) {
        return memberSurveyMappingRepository.findByMemberAndSurvey(member, survey).stream()
                .filter(item -> item.getCreatedAt().getYear() == LocalDate.now().getYear())
                .filter(item -> item.getCreatedAt().getMonth() == LocalDate.now().getMonth())
                .filter(item -> item.getCreatedAt().getDayOfMonth() == LocalDate.now().getDayOfMonth())
                .collect(Collectors.toList());
    }

    public MemberRegionMapping buildMemberRegionMappingAndSave(Member member, Region region) {
        MemberRegionMapping memberRegionMapping = MemberRegionMapping.builder()
                .regionStatus(RegionEnum.DEFAULT)
                .member(member)
                .region(region)
                .build();

        return memberRegionMappingRepository.save(memberRegionMapping);
    }

    public MemberRegionMapping buildMemberRegionMapping(Member member, Region region) {
        return MemberRegionMapping.builder()
                .regionStatus(RegionEnum.DEFAULT)
                .member(member)
                .region(region)
                .build();
    }

    public MemberRegionMapping saveMemberRegionmapping(MemberRegionMapping memberRegionMapping){
        return memberRegionMappingRepository.save(memberRegionMapping);
    }

    public MemberRegionMapping filterMemberRegionMappingList(List<MemberRegionMapping> rawList) {
        return rawList.stream()
                .filter(item -> item.getRegionStatus().equals(RegionEnum.DEFAULT))
                .findFirst()
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
    }

    public List<AnswerStatusDto> buildFromFilteredMemberSurveyMappingList(Member member, List<Long> surveyIdList) {
        return memberSurveyMappingRepository.findByMember(member)
                .stream()
                .filter(item ->
                        item.getCreatedAt().isAfter(LocalDateTime.of(LocalDate.now().minusDays(1L), LocalTime.of(23, 59))))
                .map(item -> {
                    surveyIdList.remove(item.getSurvey().getId());
                    return new AnswerStatusDto(item.getSurvey().getId(), item.getAnswer().getContent());
                })
                .collect(Collectors.toList());
    }

    public Boolean ifAnswerExist(Member member) {
        List<MemberSurveyMapping> candidateList = memberSurveyMappingRepository
                .findByCreatedAtAfter(LocalDateTime.of(LocalDate.now().minusDays(1L), LocalTime.of(23, 59)))
                .stream()
                .filter(item -> item.getMember().getMemberId().equals(member.getMemberId()))
                .collect(Collectors.toList());

        return !candidateList.isEmpty();
    }
}
