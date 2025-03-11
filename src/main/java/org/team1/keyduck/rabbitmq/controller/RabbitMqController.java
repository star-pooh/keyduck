package org.team1.keyduck.rabbitmq.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.rabbitmq.RabbitMqDto;
import org.team1.keyduck.rabbitmq.service.RabbitMqService;

@RestController
@RequiredArgsConstructor
public class RabbitMqController {

    private final RabbitMqService rabbitMqService;

    @PostMapping("/send/message")
    public ResponseEntity<ApiResponse<String>> sendMessage(
            @RequestBody RabbitMqDto dto) {
        rabbitMqService.sendMessage(dto);

        ApiResponse<String> response = ApiResponse.success(
                SuccessCode.CREATE_SUCCESS,
                "Message sent to RabbitMQ"
        );

        return new ResponseEntity<>(response, response.getStatus());
    }
}
