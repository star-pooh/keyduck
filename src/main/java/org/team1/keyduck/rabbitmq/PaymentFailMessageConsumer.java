package org.team1.keyduck.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.team1.keyduck.payment.service.PaymentProcessService;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentFailMessageConsumer {

    private final PaymentProcessService paymentProcessService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumePaymentFailMessage(String paymentKey,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        try {
            log.info("consume payment fail message - paymentKey : {}", paymentKey);

            paymentProcessService.paymentCancelProcess(paymentKey);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("move to dlq - paymentKey : {}", paymentKey);
            channel.basicNack(deliveryTag, false, false);

            throw e;
        }
    }
}
