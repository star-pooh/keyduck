package org.team1.keyduck.keyboard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.keyboard.dto.request.KeyboardCreateRequestDto;
import org.team1.keyduck.keyboard.service.KeyboardService;

@RestController
@RequestMapping("/api/keyboards")
@RequiredArgsConstructor
public class KeyboardController {
    private final KeyboardService keyboardService;

    // 키보드 생성 API
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createKeyboardAPI(
            @RequestBody @Valid KeyboardCreateRequestDto requestDto) {

        keyboardService.createKeyboard(requestDto);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.CREATE_SUCCESS),
                SuccessCode.CREATE_SUCCESS.getStatus());
    }

}