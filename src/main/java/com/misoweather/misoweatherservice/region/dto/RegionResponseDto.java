package com.misoweather.misoweatherservice.region.dto;

import com.misoweather.misoweatherservice.domain.region.Region;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class RegionResponseDto {
    // TODO Dto <T>를 활용해서 재활용할 수 있겠다.
    private List<Region> regionList;

    @Builder
    public RegionResponseDto(List<Region> midScaleList){
        this.regionList = midScaleList;
    }
}