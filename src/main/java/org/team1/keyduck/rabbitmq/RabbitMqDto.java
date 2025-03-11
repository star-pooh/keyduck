package org.team1.keyduck.rabbitmq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RabbitMqDto {

    private final String title;
    private final String content;
}
