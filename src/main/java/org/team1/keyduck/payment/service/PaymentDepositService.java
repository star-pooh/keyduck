package org.team1.keyduck.payment.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.util.ErrorCode;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import org.team1.keyduck.payment.dto.PaymentDto;
import org.team1.keyduck.payment.entity.PaymentDeposit;
import org.team1.keyduck.payment.repository.PaymentDepositRepository;

@Service
@RequiredArgsConstructor
public class PaymentDepositService {

    private final PaymentDepositRepository paymentDepositRepository;
    private final BiddingRepository biddingRepository;

    @Transactional
    public void processPaymentDeposit(PaymentDto paymentDto) {
        paymentDepositRepository.findByMember_Id(paymentDto.getMember().getId()).ifPresentOrElse(
                existPaymentDeposit -> updateExistPaymentDeposit(existPaymentDeposit,
                        paymentDto.getAmount()), () -> createNewPaymentDeposit(paymentDto));
    }

    private void updateExistPaymentDeposit(PaymentDeposit existPaymentDeposit, Long amount) {
        existPaymentDeposit.updatePaymentDeposit(amount);
    }

    private void createNewPaymentDeposit(PaymentDto paymentDto) {
        PaymentDeposit newPaymentDeposit = PaymentDeposit.builder().member(paymentDto.getMember())
                .depositAmount(paymentDto.getAmount()).build();

        paymentDepositRepository.save(newPaymentDeposit);
    }

    @Transactional
    public void payBiddingPrice(Long memberId, Long newBiddingPrice, Long lastBiddingPrice) {

        PaymentDeposit paymentDeposit = paymentDepositRepository.findByMember_Id(memberId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_MEMBER,
                        ErrorMessageParameter.MEMBER));

        if (!(newBiddingPrice - lastBiddingPrice <= paymentDeposit.getDepositAmount())) {
            throw new DataInvalidException(ErrorCode.INSUFFICIENT_PAYMENT_DEPOSIT_AMOUNT, null);
        }

        if (lastBiddingPrice == 0) {
            paymentDeposit.deductedPrice(newBiddingPrice);
        } else {
            paymentDeposit.deductedPrice(newBiddingPrice - lastBiddingPrice);
        }

    }

    @Transactional
    public void refundPaymentDeposit(Long auctionId) {
        List<Bidding> maxBiddingPricePerMember
                = biddingRepository.findAllByIdBiddingMax(auctionId);

        for (Bidding maxBiddingPrice : maxBiddingPricePerMember) {
            PaymentDeposit paymentDeposit = paymentDepositRepository
                    .findByMember_Id(maxBiddingPrice.getMember().getId())
                    .orElseThrow(() ->
                            new DataNotFoundException(ErrorCode.NOT_FOUND_MEMBER, "멤버"));

            paymentDeposit.updatePaymentDeposit(maxBiddingPrice.getPrice());
        }
    }
}
