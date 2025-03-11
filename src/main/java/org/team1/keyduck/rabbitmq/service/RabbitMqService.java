package org.team1.keyduck.rabbitmq.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.team1.keyduck.rabbitmq.RabbitMqDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqService {

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(RabbitMqDto dto) {
        log.info("message send : {}", dto.getTitle());
        log.info("message send : {}", dto.getContent());

        this.rabbitTemplate.convertAndSend(exchangeName, routingKey, dto);
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(RabbitMqDto dto) {
        log.info("Received Message : {}", dto.getTitle());
        log.info("Received Message : {}", dto.getContent());
    }
}
