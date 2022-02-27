package com.misoweather.misoweatherservice.region.service;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.mapping.service.MappingRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleRegionService {

    private final RegionService regionService;
    private final MappingRegionService mappingRegionService;

    @Transactional
    public MemberRegionMapping updateRegion(Member member, Long regionId){
        Region targetRegion = regionService.getRegion(regionId);
        List<MemberRegionMapping> memberRegionMappingList = mappingRegionService.getMemberRegionMappingList(member);
        return regionService.updateRegion(memberRegionMappingList, targetRegion);
    }
}
