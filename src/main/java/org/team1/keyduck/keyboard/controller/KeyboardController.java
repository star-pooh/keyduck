package org.team1.keyduck.keyboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.keyboard.dto.response.KeyboardReadResponseDto;
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
}
