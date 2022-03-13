package com.misoweather.misoweatherservice.controller;

import com.misoweather.misoweatherservice.global.api.ApiResponseWithData;
import com.misoweather.misoweatherservice.global.api.ListDto;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.mapping.reader.SurveyReader;
import com.misoweather.misoweatherservice.mapping.service.MappingSurveyService;
import com.misoweather.misoweatherservice.member.auth.UserDetailsImpl;
import com.misoweather.misoweatherservice.survey.dto.AnswerStatusDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyJoinDto;
import com.misoweather.misoweatherservice.survey.dto.AnswerSurveyResponseDto;
import com.misoweather.misoweatherservice.survey.service.SimpleSurveyService;
import com.misoweather.misoweatherservice.survey.service.SurveyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"서베이"})
@RestController
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;
    private final MappingSurveyService mappingSurveyService;
    private final SimpleSurveyService simpleSurveyService;

    @ApiOperation(value = "사용자의 서베이 답변 상태 가져오기")
    @GetMapping("/api/survey/member")
    public ResponseEntity<ApiResponseWithData<ListDto<AnswerStatusDto>>>
    getSurveyStatus(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(ApiResponseWithData.<ListDto<AnswerStatusDto>>builder()
                .status(HttpStatusEnum.OK)
                .data(simpleSurveyService.getAnswerStatus(userDetails.getMember()))
                .message("생성 성공")
                .build());
    }

    @ApiOperation(value = "서베이 답변 목록 가져오기")
    @GetMapping("/api/survey/answers/{surveyId}")
    public ResponseEntity<ApiResponseWithData<ListDto<AnswerSurveyJoinDto>>>
    getAnswer(@PathVariable Long surveyId) {
        return ResponseEntity.ok(ApiResponseWithData.<ListDto<AnswerSurveyJoinDto>>builder()
                .status(HttpStatusEnum.OK)
                .data(surveyService.getAnswerList(surveyId))
                .message("생성 성공")
                .build());
    }

    @ApiOperation(value = "서베이 답변 입력")
    @PostMapping("/api/survey")
    public ResponseEntity<ApiResponseWithData<AnswerSurveyResponseDto>>
    registerComment(@RequestBody AnswerSurveyDto answerSurveyDto,
                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(ApiResponseWithData.<AnswerSurveyResponseDto>builder()
                .status(HttpStatusEnum.OK)
                .data(simpleSurveyService.answerSurvey(userDetails.getMember(), answerSurveyDto))
                .message("생성 성공")
                .build());
    }

    @ApiOperation(value = "서베이 결과")
    @GetMapping("/api/survey")
    public ResponseEntity<ApiResponseWithData<ListDto<SurveyReader>>>
    getSurveryResult(@RequestParam(required = false) String shortBigScale) {
        return ResponseEntity.ok(ApiResponseWithData.<ListDto<SurveyReader>>builder()
                .status(HttpStatusEnum.OK)
                .data(simpleSurveyService.getSurveyResultList(shortBigScale))
                .message("생성 성공")
                .build());
    }

    @ApiOperation(value = "서베이 답변 여부")
    @GetMapping("/api/survey/precheck")
    public ResponseEntity<ApiResponseWithData<Boolean>>
    checkSurveyByMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(ApiResponseWithData.<Boolean>builder()
                .status(HttpStatusEnum.OK)
                .data(mappingSurveyService.ifAnswerExist(userDetails.getMember()))
                .message("생성 성공")
                .build());
    }
}
