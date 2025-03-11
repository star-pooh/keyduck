package org.team1.keyduck.payment.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.team1.keyduck.payment.dto.PaymentDto;

@Service
@RequiredArgsConstructor
public class PaymentProcessService {

    private final PaymentService paymentService;
    private final PaymentDepositService paymentDepositService;

    public void paymentConfirmProcess(String jsonBody, Long memberId) throws Exception {
        // 임시 결제 정보와 결제 승인 요청 정보가 일치하는지 확인
        paymentService.validatePaymentData(jsonBody, memberId);

        // 결제 내역 생성
        paymentService.createPayment(jsonBody, memberId);

        // 결제 승인 요청 
        JSONObject approvalPaymentResult = paymentService.approvalPaymentRequest(jsonBody);

        // 결제 승인이 완료된 경우, 결제 내역 갱신
        PaymentDto paymentDto = paymentService.updatePaymentConfirm(approvalPaymentResult);

        // 경매 포인트 생성
        paymentDepositService.processPaymentDeposit(paymentDto);
    }

    public void paymentCancelProcess(String paymentKey) throws Exception {
        // 결제 취소 요청
        JSONObject cancelPaymentResult = paymentService.cancelPaymentRequest(paymentKey);

        // 결제 취소가 완료된 경우, 결제 내역 갱신
        paymentService.updatePaymentCancel(cancelPaymentResult);
    }
}
