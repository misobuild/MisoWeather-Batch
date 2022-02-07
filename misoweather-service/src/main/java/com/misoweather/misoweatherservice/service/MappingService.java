package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.constants.RegionEnum;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMapping;
import com.misoweather.misoweatherservice.domain.member_survey_mapping.MemberSurveyMappingRepository;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.dto.response.member.MemberInfoResponseDto;
import com.misoweather.misoweatherservice.exception.ApiCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MappingService {
    private final MemberRegionMappingRepository memberRegionMappingRepository;
    private final MemberSurveyMappingRepository memberSurveyMappingRepository;

    // MemberRegionMapping
    public String getBigScale(Member member) {
        return memberRegionMappingRepository.findMemberRegionMappingByMember(member).stream()
                .filter(item -> item.getRegionStatus().equals(RegionEnum.DEFAULT))
                .map(item -> item.getRegion().getBigScale())
                .findFirst()
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
    }

    public List<MemberRegionMapping> getMemberRegionMappingList(Member member) {
        return memberRegionMappingRepository.findMemberRegionMappingByMember(member);
    }

    public MemberRegionMapping buildMemberRegionMappingAndSave(Member member, Region region){
        MemberRegionMapping memberRegionMapping = MemberRegionMapping.builder()
                .regionStatus(RegionEnum.DEFAULT)
                .member(member)
                .region(region)
                .build();

        return memberRegionMappingRepository.save(memberRegionMapping);
    }

    public void deleteMemberRegion(Member member) {
        List<MemberRegionMapping> memberRegionMappingList = memberRegionMappingRepository.findMemberRegionMappingByMember(member);
        memberRegionMappingRepository.deleteAll(memberRegionMappingList);
    }

    public MemberRegionMapping filterMemberRegionMappingList(List<MemberRegionMapping> rawList){
        return rawList.stream()
                .filter(item -> item.getRegionStatus().equals(RegionEnum.DEFAULT))
                .findFirst()
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
    }

    // MemberSurveyMapping
    public void deleteMemberSurvey(Member member) {
        List<MemberSurveyMapping> memberSurveyMappingList = memberSurveyMappingRepository.findByMember(member);
        memberSurveyMappingRepository.deleteAll(memberSurveyMappingList);
    }
}
