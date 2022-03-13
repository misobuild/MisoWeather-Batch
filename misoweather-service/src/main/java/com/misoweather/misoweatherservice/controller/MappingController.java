package com.misoweather.misoweatherservice.controller;

import com.misoweather.misoweatherservice.global.api.ApiResponseWithData;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.member.auth.UserDetailsImpl;
import com.misoweather.misoweatherservice.region.service.SimpleRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"사용자-지역"})
@RestController
@RequiredArgsConstructor
public class MappingController {
    private final SimpleRegionService simpleRegionService;

    @ApiOperation(value = "사용자 지역 정보 변경", notes = "사용자의 기본 지역을 바꿉니다")
    @PutMapping("/api/member-region-mapping/default")
    @ApiImplicitParam(name = "regionId", value = "지역 아이디", example = "15")
    public ResponseEntity<ApiResponseWithData<Long>> checkVersion(@AuthenticationPrincipal UserDetailsImpl userDetails, Long regionId) {
        return ResponseEntity.ok(ApiResponseWithData.<Long>builder()
                .status(HttpStatusEnum.OK)
                .data(simpleRegionService.updateRegion(userDetails.getMember(), regionId).getRegion().getId())
                .message("Update Successful")
                .build());
    }
}
