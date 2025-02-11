package org.team1.keyduck.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.member.dto.request.MemberCreateRequestDto;
import org.team1.keyduck.member.service.MemberService;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createMember(
        @RequestBody @Valid MemberCreateRequestDto requestDto) {
        memberService.createMember(requestDto);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.CREATE_SUCCESS),
            SuccessCode.CREATE_SUCCESS.getStatus());
    }


}
