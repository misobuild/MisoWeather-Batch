package com.misoweather.misoweatherservice.controller;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member.MemberRepository;
import com.misoweather.misoweatherservice.global.api.ApiResponse;
import com.misoweather.misoweatherservice.global.api.ApiResponseWithData;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.member.auth.JwtTokenProvider;
import com.misoweather.misoweatherservice.member.auth.UserDetailsImpl;
import com.misoweather.misoweatherservice.member.dto.*;
import com.misoweather.misoweatherservice.member.service.MemberService;
import com.misoweather.misoweatherservice.member.service.SimpleMemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class MemberController {
    private final Environment env;
    private final MemberService memberService;
    private final SimpleMemberService simpleMemberService;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "회원 가입")
    @PostMapping("api/member")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody SignUpRequestDto signUpRequestDto
            , @RequestParam String socialToken) throws ParseException {
        Member registeredMember = simpleMemberService.registerMember(signUpRequestDto, socialToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("serverToken", jwtTokenProvider
                .createToken(Long.toString(registeredMember.getMemberId()),
                        registeredMember.getSocialId(),
                        registeredMember.getSocialType()));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(new ApiResponse(HttpStatusEnum.OK, "Register Successful"));
    }

    @ApiOperation(value= "회원 정보 가져오기")
    @GetMapping("api/member")
    public ResponseEntity<ApiResponseWithData<MemberInfoResponseDto>> getMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(ApiResponseWithData.<MemberInfoResponseDto>builder()
                .status(HttpStatusEnum.OK)
                .message("멤버 정보")
                .data(simpleMemberService.getMemberInfo(userDetails.getMember()))
                .build());
    }

    // TODO loginREquestDTO 상속해서 아래 login에서 polymorphism 적용하기
    @ApiOperation(value= "미소웨더 토큰 재발급")
    @PostMapping("api/member/token")
    public ResponseEntity<ApiResponse> reissue(@RequestBody LoginRequestDto loginRequestDto
            , @RequestParam String socialToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("serverToken", simpleMemberService.reissue(loginRequestDto, socialToken));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(new ApiResponse(HttpStatusEnum.OK, "Token Generated"));
    }

    @ApiOperation(value= "사용 가능 닉네임 조회하기")
    @GetMapping("api/member/nickname")
    public ResponseEntity<ApiResponseWithData<NicknameResponseDto>> buildNickname() {
        return ResponseEntity.ok(ApiResponseWithData.<NicknameResponseDto>builder()
                .status(HttpStatusEnum.OK)
                .message("생성된 닉네임과 해당 emoji")
                .data(memberService.buildNickname())
                .build());
    }

    @ApiOperation(value= "회원 삭제")
    @DeleteMapping("api/member")
    public ResponseEntity<ApiResponse> deleteMember(@RequestBody DeleteMemberRequestDto deleteMemberRequestDto) {
        simpleMemberService.deleteMember(deleteMemberRequestDto);

        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatusEnum.OK, "Deletetion Successful"));
    }

    @ApiOperation(value="회원 가입 여부")
    @GetMapping("api/member/existence")
    public ResponseEntity<ApiResponseWithData<Boolean>> checkExistence(@RequestParam String socialId, @RequestParam String socialType){
        return ResponseEntity.ok(ApiResponseWithData.<Boolean>builder()
                .status(HttpStatusEnum.OK)
                .message("ifExist: Boolean.TRUE")
                .data(memberService.ifMemberExistDelete(socialId, socialType))
                .build());
    }

    @GetMapping("/health_check")
    public String status(){
        return String.format("It's working in Misoweather Service"
                + " on port " + env.getProperty("local.server.port")
                + " with tokenSecret " + env.getProperty("app.auth.tokenSecret")
        );
    }

}
