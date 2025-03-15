package org.team1.keyduck.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.common.exception.SuccessCode;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.payment.dto.TempPaymentRequestDto;
import org.team1.keyduck.payment.service.PaymentProcessService;
import org.team1.keyduck.payment.service.TempPaymentService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final TempPaymentService tempPaymentService;
    private final PaymentProcessService paymentProcessService;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyCustomerRole(
            @AuthenticationPrincipal AuthMember authMember) {
        MemberRole memberRole = authMember.getMemberRole();

        if (memberRole.equals(MemberRole.SELLER)) {
            throw new OperationNotAllowedException(ErrorCode.FORBIDDEN_PAYMENT_FROM_SELLER, null);
        }

        return new ResponseEntity<>(ApiResponse.success(SuccessCode.READ_SUCCESS),
                SuccessCode.READ_SUCCESS.getStatus());
    }

    @PostMapping("/temp")
    public ResponseEntity<ApiResponse<Void>> saveTempPaymentInfo(
            @AuthenticationPrincipal AuthMember authMember,
            @RequestBody TempPaymentRequestDto dto) {
        // memberId, orderId, amount는 결제 요청과 승인 사이에 데이터 무결성을 확인하기 위해 필요하므로 서버에 임시 저장
        tempPaymentService.createTempPayment(authMember.getId(), dto.getOrderId(),
                dto.getPaymentAmount());

        return new ResponseEntity<>(ApiResponse.success(SuccessCode.CREATE_SUCCESS),
                SuccessCode.CREATE_SUCCESS.getStatus());
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmPayment(
            @AuthenticationPrincipal AuthMember authMember,
            @RequestBody String jsonBody) throws Exception {
        // 결제 승인 정보를 결제 내역 DB에 저장
        paymentProcessService.paymentConfirmProcess(jsonBody, authMember.getId());
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.CREATE_SUCCESS),
                SuccessCode.CREATE_SUCCESS.getStatus());
    }
}
