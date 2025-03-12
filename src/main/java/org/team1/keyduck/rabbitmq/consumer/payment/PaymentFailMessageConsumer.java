package org.team1.keyduck.rabbitmq.consumer.payment;

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

    @RabbitListener(queues = "${rabbitmq.queue.name}", ackMode = "MANUAL")
    public void consumePaymentFailMessage(String paymentKey,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        try {
            paymentProcessService.paymentCancelProcess(paymentKey);
            // 결제 취소 요청에 성공한 메시지를 삭제함
            channel.basicAck(deliveryTag, false);

            log.info("consume payment fail message - paymentKey : {}", paymentKey);
        } catch (Exception e) {
            // 결제 취소 요청에 실패한 메시지를 DLQ로 보냄
            channel.basicNack(deliveryTag, false, false);

            log.error("move to dlq - paymentKey : {}", paymentKey);
            throw e;
        }
    }
}
