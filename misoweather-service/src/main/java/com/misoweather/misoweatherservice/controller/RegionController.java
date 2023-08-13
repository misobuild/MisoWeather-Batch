package com.misoweather.misoweatherservice.controller;

import com.misoweather.misoweatherservice.global.api.ApiResponseWithData;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.member.auth.UserDetailsImpl;
import com.misoweather.misoweatherservice.region.dto.RegionResponseDto;
import com.misoweather.misoweatherservice.region.service.RegionService;
import com.misoweather.misoweatherservice.region.service.SimpleRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"지역"})
@RestController
@RequestMapping("/misoweather-service")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;
    private final SimpleRegionService simpleRegionService;

    @ApiOperation(value = "2단계 지역 리스트 요청", notes = "1단계에서 선택한 bigScaleRegion에 따른 2단계 지역리스트를 중복 없이 id 순서대로 보여준다")
    @ApiImplicitParam(name = "bigScaleRegion", value = "큰 단위 지역 구분 (유의: 세종특별자치시, 제주도)", example = "세종특별자치시")
    @GetMapping("/region/{bigScaleRegion}")
    public ResponseEntity<ApiResponseWithData<RegionResponseDto>> getMidScaleList(@PathVariable(name = "bigScaleRegion") String bigScaleRegion) {
        return ResponseEntity.ok(ApiResponseWithData.<RegionResponseDto>builder()
                .status(HttpStatusEnum.OK)
                .data(regionService.getMidScaleList(bigScaleRegion))
                .message("생성 성공")
                .build());
    }

    @ApiOperation(value = "3단계 지역 리스트 요청", notes = "2단계에서 선택한 midScaleRegion에 따른 3단계 지역리스트를 id 순서대로 보여준다")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bigScaleRegion", value = "가장 큰 단위의 지역 구분 (유의: 세종특별자치시, 제주도)", example = "경기도"),
            @ApiImplicitParam(name = "midScaleRegion", value= "중간 단위 지역 구분 midScaleRegion (특이사례: 고양시덕양구)", example = "고양시덕양구")
    })
    @GetMapping("/region/{bigScaleRegion}/{midScaleRegion}")
    public ResponseEntity<ApiResponseWithData<RegionResponseDto>> getSmallScaleList(@PathVariable(name = "bigScaleRegion") String bigScaleRegion, @PathVariable(name = "midScaleRegion") String midScaleRegion) {
        return ResponseEntity.ok(ApiResponseWithData.<RegionResponseDto>builder()
                .status(HttpStatusEnum.OK)
                .message("bigScale과 midScale로 찾아온 smallScale 리스트")
                .data(regionService.getSmallScaleList(bigScaleRegion, midScaleRegion))
                .build());
    }

    @ApiOperation(value = "사용자 지역 정보 변경", notes = "사용자의 기본 지역을 바꿉니다")
    @PutMapping("/member-region-mapping/default")
    @ApiImplicitParam(name = "regionId", value = "지역 아이디", example = "15")
    public ResponseEntity<ApiResponseWithData<Long>> updateMemberRegion(@AuthenticationPrincipal UserDetailsImpl userDetails, Long regionId) {
        return ResponseEntity.ok(ApiResponseWithData.<Long>builder()
                .status(HttpStatusEnum.OK)
                .data(simpleRegionService.updateRegion(userDetails.getMember(), regionId).getRegion().getId())
                .message("Update Successful")
                .build());
    }
}
