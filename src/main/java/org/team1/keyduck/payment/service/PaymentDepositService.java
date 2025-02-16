package org.team1.keyduck.payment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.keyduck.common.exception.BiddingNotAvailableException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.payment.dto.PaymentDto;
import org.team1.keyduck.payment.entity.PaymentDeposit;
import org.team1.keyduck.payment.repository.PaymentDepositRepository;

@Service
@RequiredArgsConstructor
public class PaymentDepositService {

    private final PaymentDepositRepository paymentDepositRepository;

    @Transactional
    public void processPaymentDeposit(PaymentDto paymentDto) {
        paymentDepositRepository
                .findByMember_Id(paymentDto.getMember().getId())
                .ifPresentOrElse(
                        existPaymentDeposit -> updateExistPaymentDeposit(existPaymentDeposit,
                                paymentDto.getAmount()),
                        () -> createNewPaymentDeposit(paymentDto)
                );
    }

    private void updateExistPaymentDeposit(PaymentDeposit existPaymentDeposit, Long amount) {
        existPaymentDeposit.updatePaymentDeposit(amount);
    }

    private void createNewPaymentDeposit(PaymentDto paymentDto) {
        PaymentDeposit newPaymentDeposit = PaymentDeposit.builder()
                .member(paymentDto.getMember())
                .depositAmount(paymentDto.getAmount())
                .build();

        paymentDepositRepository.save(newPaymentDeposit);
    }

    @Transactional
    public void payBiddingPrice(Long memberId, Long newBiddingPrice, Long lastBiddingPrice) {

        PaymentDeposit paymentDeposit = paymentDepositRepository.findByMember_Id(memberId)
                .orElseThrow(
                        () -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND)
                );
        if (!(newBiddingPrice - lastBiddingPrice < paymentDeposit.getDepositAmount())) {
            throw new BiddingNotAvailableException(ErrorCode.INSUFFICIENT_PAYMENT_AMOUNT);
        }

        if (lastBiddingPrice == 0) {
            paymentDeposit.deductedPrice(newBiddingPrice);
        } else {
            paymentDeposit.deductedPrice(newBiddingPrice - lastBiddingPrice);
        }

    }
}
