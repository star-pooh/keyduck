package org.team1.keyduck.keyboard.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.keyboard.dto.request.KeyboardCreateRequestDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardCreateResponseDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardReadResponseDto;
import org.team1.keyduck.keyboard.service.KeyboardService;

@RestController
@RequestMapping("/api/keyboards")
@RequiredArgsConstructor
public class KeyboardController {

    private final KeyboardService keyboardService;

    // 키보드 생성 API
    @PostMapping
    public ResponseEntity<ApiResponse<KeyboardCreateResponseDto>> createKeyboardAPI(
            @AuthenticationPrincipal AuthMember authMember,
            @RequestBody @Valid KeyboardCreateRequestDto requestDto) {

        KeyboardCreateResponseDto response = keyboardService.createKeyboard(authMember.getId(),
                requestDto);

        return new ResponseEntity<>(ApiResponse.success(SuccessCode.CREATE_SUCCESS, response),
                SuccessCode.CREATE_SUCCESS.getStatus());
    }

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<KeyboardReadResponseDto>>> findKeyboardBySellerId(
            @AuthenticationPrincipal AuthMember authMember) {

        List<KeyboardReadResponseDto> keyboardReadResponseDto = keyboardService.findKeyboardBySellerId(
                authMember.getId());

        ApiResponse<List<KeyboardReadResponseDto>> response = ApiResponse.success(
                SuccessCode.READ_SUCCESS, keyboardReadResponseDto);

        return new ResponseEntity<>(response, response.getStatus());
    }

}

