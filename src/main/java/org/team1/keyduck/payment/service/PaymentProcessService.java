package org.team1.keyduck.payment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.keyduck.payment.dto.PaymentDto;

@Service
@RequiredArgsConstructor
public class PaymentProcessService {

    private final PaymentService paymentService;
    private final PaymentDepositService paymentDepositService;

    @Transactional
    public void paymentProcess(String jsonBody, Long memberId) throws Exception {
        PaymentDto paymentDto = paymentService.createPayment(jsonBody, memberId);
        paymentDepositService.processPaymentDeposit(paymentDto);
    }
}
