package com.misoweather.misoweatherservice.mapping.service;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.global.constants.RegionEnum;
import com.misoweather.misoweatherservice.global.exception.ApiCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MappingRegionService {

    private final MemberRegionMappingRepository memberRegionMappingRepository;

    public List<MemberRegionMapping> getMemberRegionMappingList(Member member) {
        return memberRegionMappingRepository.findMemberRegionMappingByMember(member);
    }

    public void deleteMemberRegion(Member member) {
        List<MemberRegionMapping> memberRegionMappingList = memberRegionMappingRepository.findMemberRegionMappingByMember(member);
        memberRegionMappingRepository.deleteAll(memberRegionMappingList);
    }

    public String getBigScale(Member member) {
        return memberRegionMappingRepository.findMemberRegionMappingByMember(member).stream()
                .filter(item -> item.getRegionStatus().equals(RegionEnum.DEFAULT))
                .map(item -> item.getRegion().getBigScale())
                .findFirst()
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
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

    public MemberRegionMapping filterMemberRegionMappingList(List<MemberRegionMapping> rawList) {
        return rawList.stream()
                .filter(item -> item.getRegionStatus().equals(RegionEnum.DEFAULT))
                .findFirst()
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
    }

    public MemberRegionMapping saveMemberRegionmapping(MemberRegionMapping memberRegionMapping){
        return memberRegionMappingRepository.save(memberRegionMapping);
    }
}
