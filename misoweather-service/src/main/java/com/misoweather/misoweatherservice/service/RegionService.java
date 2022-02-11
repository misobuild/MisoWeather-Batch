package com.misoweather.misoweatherservice.service;

import com.misoweather.misoweatherservice.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.constants.BigScaleEnum;
import com.misoweather.misoweatherservice.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.constants.RegionEnum;
import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMapping;
import com.misoweather.misoweatherservice.domain.member_region_mapping.MemberRegionMappingRepository;
import com.misoweather.misoweatherservice.domain.region.Region;
import com.misoweather.misoweatherservice.domain.region.RegionRepository;
import com.misoweather.misoweatherservice.dto.response.region.RegionResponseDto;
import com.misoweather.misoweatherservice.exception.ApiCustomException;
import com.misoweather.misoweatherservice.utils.reader.RegionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionService {

    private final RegionRepository regionRepository;
    private final RegionReader regionReader;

    public Region getRegion(Long regionId){
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.NOT_FOUND));
    }

    public RegionResponseDto getMidScaleList(String regionName) {
        String bigScale = BigScaleEnum.valueOf(regionName).getValue();
        List<Region> rawMidScaleList = regionRepository.findByBigScale(bigScale);
        return RegionResponseDto.builder()
                .midScaleList(regionReader.filterMidScaleList(rawMidScaleList))
                .build();
    }

    public RegionResponseDto getSmallScaleList(String bigScale, String midScale) {
        List<Region> rawSmallScaleList = regionRepository.findByBigScaleAndMidScale(bigScale, midScale);
        return RegionResponseDto.builder()
                .midScaleList(rawSmallScaleList)
                .build();
    }

    // updateRegionAfter 50번째 줄에 있는 memberRegionMapping
    public MemberRegionMapping updateRegion(List<MemberRegionMapping> memberRegionMappingList, Region targetRegion){
        return memberRegionMappingList.stream()
                .filter(item -> item.getRegionStatus().equals(RegionEnum.DEFAULT))
                .findFirst()
                .map(item -> item.update(targetRegion))
                .orElseThrow(() -> new ApiCustomException(HttpStatusEnum.CONFLICT));
    }
}
