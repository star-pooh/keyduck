package org.team1.keyduck.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.team1.keyduck.common.config.RabbitMqProperties;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentFailMessagePublisher {

    private final RabbitMqProperties rabbitMqProperties;
    private final RabbitTemplate rabbitTemplate;

    public void publishPaymentFailMessage(String paymentKey) {
        log.info("publish payment fail message - paymentKey : {}", paymentKey);

        rabbitTemplate.convertAndSend(rabbitMqProperties.getExchangeName(),
                rabbitMqProperties.getRoutingKey(), paymentKey);
    }
}
