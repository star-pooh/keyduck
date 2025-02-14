package org.team1.keyduck.keyboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.keyboard.service.KeyboardService;

@RestController
@RequestMapping("/api/keyboards")
@RequiredArgsConstructor
public class KeyboardController {
    private final KeyboardService keyboardService;

    // 키보드 삭제 API
    @DeleteMapping("/{keyboardId}")
    public ResponseEntity<ApiResponse<Void>> deleteKeyboardAPI(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long keyboardId) {

        keyboardService.deleteKeyboard(keyboardId, authMember.getId());

        return new ResponseEntity<>(ApiResponse.success(SuccessCode.DELETE_SUCCESS),
                SuccessCode.DELETE_SUCCESS.getStatus());
    }

}

