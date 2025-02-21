package org.team1.keyduck.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import org.team1.keyduck.payment.entity.TempPayment;
import org.team1.keyduck.payment.repository.TempPaymentRepository;

@Service
@RequiredArgsConstructor
public class TempPaymentService {

    private final TempPaymentRepository tempPaymentRepository;

    public void createTempPayment(Long memberId, String orderId, Long amount) {

        TempPayment tempPayment = TempPayment.builder()
                .memberId(memberId)
                .orderId(orderId)
                .amount(amount)
                .build();

        tempPaymentRepository.save(tempPayment);
    }

    public boolean validPaymentAmount(Long memberId, String orderId, Long amount) {
        TempPayment tempPayment = tempPaymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new DataNotFoundException(
                        ErrorCode.NOT_FOUND_TEMP_PAYMENT, ErrorMessageParameter.TEMP_PAYMENT_INFO));

        return tempPayment.getMemberId().equals(memberId)
                && tempPayment.getAmount().equals(amount);
    }
}
