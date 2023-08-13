package com.misoweather.misoweatherservice.domain.region;

import com.misoweather.misoweatherservice.domain.region.Region;
import lombok.Builder;

import java.time.LocalDateTime;

public class RegionBuilder {
    public static Region build(Long id, String bigScale, String midScale, String smallScale){
        return new Region(id, bigScale, midScale, smallScale);
    }
}
