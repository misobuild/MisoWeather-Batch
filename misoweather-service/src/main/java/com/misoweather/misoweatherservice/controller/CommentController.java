package com.misoweather.misoweatherservice.comment.presentation;

import com.misoweather.misoweatherservice.comment.dto.CommentListResponseDto;
import com.misoweather.misoweatherservice.comment.dto.CommentRegisterRequestDto;
import com.misoweather.misoweatherservice.comment.dto.CommentRegisterResponseDto;
import com.misoweather.misoweatherservice.comment.service.CommentService;
import com.misoweather.misoweatherservice.comment.service.SimpleCommentService;
import com.misoweather.misoweatherservice.global.api.ApiResponseWithData;
import com.misoweather.misoweatherservice.global.constants.HttpStatusEnum;
import com.misoweather.misoweatherservice.member.auth.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"한줄평"})
@RestController
@RequiredArgsConstructor
public class CommentController {
    private static final Integer DEFAULT_SIZE = 21;
    private final CommentService commentService;
    private final SimpleCommentService simpleCommentService;

    @ApiOperation(value = "코멘트 등록")
    @PostMapping("/api/comment")
    public ResponseEntity<ApiResponseWithData<CommentRegisterResponseDto>>
    registerComment(@RequestBody CommentRegisterRequestDto commentRegisterRequestDto,
                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(ApiResponseWithData.<CommentRegisterResponseDto>builder()
                .status(HttpStatusEnum.OK)
                .data(simpleCommentService.registerComment(commentRegisterRequestDto, userDetails.getMember()))
                .message("생성 성공")
                .build());
    }

    @ApiOperation(value = "코멘트 조회")
    @GetMapping("/api/comment")
    public ResponseEntity<ApiResponseWithData<CommentListResponseDto>>
    getCommentList(@RequestParam(required = false) Long commentId, @RequestParam Integer size) {
        if (size == null) size = DEFAULT_SIZE;
        return ResponseEntity.ok(ApiResponseWithData.<CommentListResponseDto>builder()
                .status(HttpStatusEnum.OK)
                .data(simpleCommentService.getCommentList(commentId, PageRequest.of(0, size)))
                .message("생성 성공")
                .build());
    }
}
