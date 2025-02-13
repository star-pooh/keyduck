package org.team1.keyduck.keyboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.keyboard.dto.request.KeyboardUpdateRequestDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardReadResponseDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardUpdateResponseDto;
import org.team1.keyduck.keyboard.service.KeyboardService;

import java.util.List;

@RestController
@RequestMapping("/api/keyboards")
@RequiredArgsConstructor
public class KeyboardController {

    private final KeyboardService keyboardService;


    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<KeyboardReadResponseDto>>> findKeyboardBySellerId(Authentication authentication) {

        AuthMember authMember = (AuthMember) authentication.getPrincipal();

        Long sellerId = authMember.getId();

        List<KeyboardReadResponseDto> keyboardReadResponseDto = keyboardService.findKeyboardBySellerIdService(sellerId);

        ApiResponse<List<KeyboardReadResponseDto>> response = ApiResponse.success(SuccessCode.READ_SUCCESS, keyboardReadResponseDto);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping("/{keyboardId}")
    public ResponseEntity<ApiResponse<KeyboardUpdateResponseDto>> keyboardModification(
            Authentication authentication,
            @PathVariable Long keyboardId,
            @RequestBody KeyboardUpdateRequestDto requestDto
    ) {
        AuthMember authMember = (AuthMember) authentication.getPrincipal();

        Long sellerId = authMember.getId();

        KeyboardUpdateResponseDto keyboardUpdateResponseDto = keyboardService.keyboardModificationService(sellerId, keyboardId, requestDto);

        ApiResponse<KeyboardUpdateResponseDto> response = ApiResponse.success(SuccessCode.UPDATE_SUCCESS, keyboardUpdateResponseDto);

        return new ResponseEntity<>(response, response.getStatus());

    }
}
