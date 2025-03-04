package org.team1.keyduck.email.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.email.dto.GeneralEmailRequestDto;
import org.team1.keyduck.email.service.EmailService;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> sendEmail(
            @RequestBody GeneralEmailRequestDto generalEmailRequestDto) {
        emailService.sendMail(generalEmailRequestDto);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.SEND_SUCCESS),
                SuccessCode.SEND_SUCCESS.getStatus());
    }
}
