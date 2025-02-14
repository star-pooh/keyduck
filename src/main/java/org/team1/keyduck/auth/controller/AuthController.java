package org.team1.keyduck.auth.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.auth.dto.request.MemberCreateRequestDto;
import org.team1.keyduck.auth.dto.request.SigninRequestDto;
import org.team1.keyduck.auth.dto.response.SigninResponseDto;
import org.team1.keyduck.auth.service.AuthService;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<SigninResponseDto>> signin(
        @Valid @RequestBody SigninRequestDto signinRequestDto) {
        return new ResponseEntity<>(
            ApiResponse.success(SuccessCode.LOGIN_SUCCESS, authService.login(signinRequestDto)),
            SuccessCode.LOGIN_SUCCESS.getStatus());
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Void>> joinMember(
        @RequestBody @Valid MemberCreateRequestDto requestDto) {
        authService.joinMember(requestDto);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.CREATE_SUCCESS),
            SuccessCode.CREATE_SUCCESS.getStatus());
    }
}
