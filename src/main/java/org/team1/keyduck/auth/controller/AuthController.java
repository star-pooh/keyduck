package org.team1.keyduck.auth.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.auth.dto.request.SigninRequest;
import org.team1.keyduck.auth.dto.response.SigninResponse;
import org.team1.keyduck.auth.service.AuthService;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.member.dto.request.MemberCreateRequestDto;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public SigninResponse login(@Valid @RequestBody SigninRequest signinRequest) {
        return authService.login(signinRequest);
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Void>> joinMember(
        @RequestBody @Valid MemberCreateRequestDto requestDto) {
        authService.joinMember(requestDto);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.CREATE_SUCCESS),
            SuccessCode.CREATE_SUCCESS.getStatus());
    }
}
