package org.team1.keyduck.auth.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.team1.keyduck.auth.dto.request.MemberCreateRequestDto;
import org.team1.keyduck.auth.dto.request.PaymentFormRequestDto;
import org.team1.keyduck.auth.dto.request.SigninRequestDto;
import org.team1.keyduck.auth.dto.response.MemberCreateResponseDto;
import org.team1.keyduck.auth.dto.response.PaymentFormResponseDto;
import org.team1.keyduck.auth.dto.response.SigninResponseDto;
import org.team1.keyduck.auth.service.AuthService;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.member.entity.MemberRole;

@Controller
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

    @PostMapping("/join/customer")
    public ResponseEntity<ApiResponse<MemberCreateResponseDto>> joinCustomer(
            @RequestBody @Valid MemberCreateRequestDto requestDto) {
        ApiResponse<MemberCreateResponseDto> response = ApiResponse.success(
                SuccessCode.CREATE_SUCCESS,
                authService.joinMember(requestDto, MemberRole.CUSTOMER));
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/join/seller")
    public ResponseEntity<ApiResponse<MemberCreateResponseDto>> joinSeller(
            @RequestBody @Valid MemberCreateRequestDto requestDto) {
        ApiResponse<MemberCreateResponseDto> response = ApiResponse.success(
                SuccessCode.CREATE_SUCCESS, authService.joinMember(requestDto, MemberRole.SELLER));
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/payment")
    public ResponseEntity<ApiResponse<PaymentFormResponseDto>> paymentFormSignin(
            @RequestBody PaymentFormRequestDto dto, HttpServletResponse response, Model model) {
        PaymentFormResponseDto responseDto = authService.paymentFormLogin(dto);
        String bearerToken = URLEncoder.encode(responseDto.getToken(), StandardCharsets.UTF_8);

        response.setHeader("Authorization", bearerToken);
        model.addAttribute("paymentAmount", dto.getAmount());

        return new ResponseEntity<>(ApiResponse.success(SuccessCode.CREATE_SUCCESS, responseDto),
                SuccessCode.CREATE_SUCCESS.getStatus());
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyJwtToken(HttpServletRequest request) {
        authService.verifyJwtToken(request);

        return new ResponseEntity<>(ApiResponse.success(SuccessCode.TOKEN_VERIFY_SUCCESS),
                SuccessCode.TOKEN_VERIFY_SUCCESS.getStatus());
    }
}
