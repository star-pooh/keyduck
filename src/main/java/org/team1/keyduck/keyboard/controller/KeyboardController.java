package org.team1.keyduck.keyboard.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.keyboard.dto.request.KeyboardUpdateRequestDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardReadResponseDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardUpdateResponseDto;
import org.team1.keyduck.keyboard.service.KeyboardService;

@RestController
@RequestMapping("/api/keyboards")
@RequiredArgsConstructor
public class KeyboardController {

    private final KeyboardService keyboardService;

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<KeyboardReadResponseDto>>> findKeyboardBySellerId(
            @AuthenticationPrincipal AuthMember authMember
    ) {
        List<KeyboardReadResponseDto> keyboardReadResponseDto = keyboardService.findKeyboardBySellerIdService(
                authMember.getId());

        ApiResponse<List<KeyboardReadResponseDto>> response = ApiResponse.success(
                SuccessCode.READ_SUCCESS, keyboardReadResponseDto);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping("/{keyboardId}")
    public ResponseEntity<ApiResponse<KeyboardUpdateResponseDto>> keyboardModification(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long keyboardId,
            @RequestBody KeyboardUpdateRequestDto requestDto
    ) {
        KeyboardUpdateResponseDto keyboardUpdateResponseDto = keyboardService.keyboardModificationService(
                authMember.getId(), keyboardId, requestDto);

        ApiResponse<KeyboardUpdateResponseDto> response = ApiResponse.success(
                SuccessCode.UPDATE_SUCCESS, keyboardUpdateResponseDto);

        return new ResponseEntity<>(response, response.getStatus());

    }
}
