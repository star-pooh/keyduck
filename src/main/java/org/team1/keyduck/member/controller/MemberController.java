package org.team1.keyduck.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.member.dto.request.MemberUpdatePasswordRequestDto;
import org.team1.keyduck.member.dto.request.MemberUpdateRequestDto;
import org.team1.keyduck.member.dto.response.MemberReadResponseDto;
import org.team1.keyduck.member.dto.response.MemberUpdateResponseDto;
import org.team1.keyduck.member.service.MemberService;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PatchMapping
    public ResponseEntity<ApiResponse<MemberUpdateResponseDto>> updateMember(
        @AuthenticationPrincipal AuthMember authMember,
        @RequestBody MemberUpdateRequestDto requestDto) {
        ApiResponse<MemberUpdateResponseDto> response = ApiResponse.success(
            SuccessCode.UPDATE_SUCCESS, memberService.updateMember(requestDto, authMember.getId()));
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping("/update/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
        @AuthenticationPrincipal AuthMember authMember,
        @Valid @RequestBody MemberUpdatePasswordRequestDto requestDto) {
        memberService.updatePassword(requestDto, authMember.getId());
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.UPDATE_SUCCESS),
            SuccessCode.UPDATE_SUCCESS.getStatus());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteMember(
        @AuthenticationPrincipal AuthMember authMember
    ) {
        memberService.deleteMember(authMember.getId());
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.DELETE_SUCCESS),
            SuccessCode.DELETE_SUCCESS.getStatus());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MemberReadResponseDto>> getMember(
        @AuthenticationPrincipal AuthMember authMember
    ) {
        ApiResponse<MemberReadResponseDto> response = ApiResponse.success(
            SuccessCode.READ_SUCCESS, memberService.getMember(authMember.getId())
        );
        return new ResponseEntity<>(response, response.getStatus());
    }
}
