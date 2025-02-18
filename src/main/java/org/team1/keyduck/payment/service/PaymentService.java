package org.team1.keyduck.payment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.dto.PaymentDto;
import org.team1.keyduck.payment.entity.Payment;
import org.team1.keyduck.payment.processor.PaymentProcessor;
import org.team1.keyduck.payment.repository.PaymentRepository;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TempPaymentService tempPaymentService;
    private final MemberRepository memberRepository;

    private final PaymentProcessor paymentProcessor;

    @Transactional
    public PaymentDto createPayment(String jsonBody, Long memberId) throws Exception {
        Member foundedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_USER, "멤버"));

        JSONObject jsonObject = paymentProcessor.parseJsonBody(jsonBody);

        // 클라이언트에서 결제 금액을 조작하는 행위를 방지하기 위해 결제 승인 전에 결제 금액을 확인
        validPaymentAmount(jsonObject, memberId);

        // 결제 승인 API 호출
        JSONObject resultJsonObject = paymentProcessor.requestPaymentApproval(jsonObject);

        // 결제 내역 DB에 데이터 저장
        Payment payment = paymentProcessor.createPaymentData(resultJsonObject, foundedMember);
        Payment savedPayment = paymentRepository.save(payment);

        return PaymentDto.of(savedPayment);
    }

    private void validPaymentAmount(JSONObject jsonObject, Long memberId) {
        String orderId = (String) jsonObject.get("orderId");
        Long amount = Long.valueOf((String) jsonObject.get("amount"));

        boolean result = tempPaymentService.validPaymentAmount(memberId, orderId, amount);

        if (!result) {
            // 클라이언트가 결제 금액을 조작한 경우 결제 취소
            throw new DataInvalidException(ErrorCode.INVALID_DATA_VALUE, "결제 금액");
        }
    }
}
