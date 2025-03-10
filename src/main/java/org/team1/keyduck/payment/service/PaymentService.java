package org.team1.keyduck.payment.service;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.PaymentConfirmException;
import org.team1.keyduck.common.util.Constants;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.dto.PaymentDto;
import org.team1.keyduck.payment.entity.Payment;
import org.team1.keyduck.payment.processor.PaymentProcessor;
import org.team1.keyduck.payment.repository.PaymentRepository;
import org.team1.keyduck.payment.util.PaymentConfirmErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TempPaymentService tempPaymentService;
    private final MemberRepository memberRepository;
    private final PaymentProcessor paymentProcessor;

    public void validatePaymentData(String jsonBody, Long memberId) {
        JSONObject jsonObject = paymentProcessor.parseJsonBody(jsonBody);

        // 클라이언트에서 결제 금액을 조작하는 행위를 방지하기 위해 결제 승인 요청 전에 결제 금액을 확인
        validatePaymentAmount(jsonObject, memberId);
    }

    @Transactional
    public void createPayment(String jsonBody, Long memberId) {
        Member foundedMember = memberRepository.findByIdAndIsDeleted(memberId, false)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_MEMBER,
                        ErrorMessageParameter.MEMBER));

        JSONObject jsonObject = paymentProcessor.parseJsonBody(jsonBody);
        Payment payment = paymentProcessor.getCreatePaymentData(jsonObject, foundedMember);

        paymentRepository.save(payment);
    }

    public JSONObject approvalPaymentRequest(String jsonBody) throws Exception {
        JSONObject jsonObject = paymentProcessor.parseJsonBody(jsonBody);
        // 멱등키 생성
        UUID idempotencyKey = UUID.randomUUID();

        for (int attempt = 0; attempt <= Constants.PAYMENT_CONFIRM_MAX_RETRIES; attempt++) {
            try {
                // 결제 승인 요청
                JSONObject approvalResult = paymentProcessor.approvalPaymentRequest(jsonObject,
                        idempotencyKey);

                String code = (String) approvalResult.get("code");

                // 결제 승인 에러 발생 시 예외 발생
                if (code != null) {
                    throw new PaymentConfirmException(PaymentConfirmErrorCode.valueOf(code));
                }

                return approvalResult;
            } catch (IOException e) {
                // 네트워크 문제로 인한 에러의 경우 재요청 시도
                String paymentKey = (String) jsonObject.get("paymentKey");
                String orderId = (String) jsonObject.get("orderId");

                // 재요청 시도 최대 횟수에 도달한 경우 예외 발생
                if (attempt == Constants.PAYMENT_CONFIRM_MAX_RETRIES) {
                    log.error("결제 승인 재요청 시도 횟수 소진 - paymentKey : {}, orderId : {}", paymentKey,
                            orderId);

                    throw new PaymentConfirmException(
                            PaymentConfirmErrorCode.FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING);
                }

                // 지수백오프를 활용하여 재요청 주기 설정(2초, 4초, 8초)
                long backoffTime = (long) Math.pow(2, attempt + 1) * 1000;
                log.warn("paymentKey : {}, orderId : {}, {}번째 재시도", paymentKey, orderId,
                        attempt + 1);
                Thread.sleep(backoffTime);
            } catch (Exception e) {
                log.error("결제 승인 중 예기치 못한 예외 발생");
                throw e;
            }
        }
        return null;
    }

    @Transactional
    public PaymentDto updatePayment(JSONObject jsonObject) {
        String paymentKey = (String) jsonObject.get("paymentKey");
        Payment foundedPayment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_PAYMENT,
                        ErrorMessageParameter.PAYMENT_INFO));

        // 결제 승인 데이터 생성
        Payment confirmPaymentData = paymentProcessor.getConfirmPaymentData(jsonObject);
        foundedPayment.updatePaymentInfo(confirmPaymentData);

        return PaymentDto.of(foundedPayment);
    }

    private void validatePaymentAmount(JSONObject jsonObject, Long memberId) {
        String orderId = (String) jsonObject.get("orderId");
        Long amount = Long.valueOf((String) jsonObject.get("amount"));

        boolean result = tempPaymentService.validPaymentAmount(memberId, orderId, amount);

        if (!result) {
            // 클라이언트가 결제 금액을 조작한 경우 결제 취소
            throw new DataInvalidException(ErrorCode.INVALID_DATA_VALUE,
                    ErrorMessageParameter.PAYMENT_AMOUNT);
        }
    }
}
