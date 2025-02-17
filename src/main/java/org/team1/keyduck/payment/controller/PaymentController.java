package org.team1.keyduck.payment.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.payment.service.PaymentProcessService;
import org.team1.keyduck.payment.service.TempPaymentService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final TempPaymentService tempPaymentService;
    private final PaymentProcessService paymentProcessService;

    @GetMapping("/process")
    public String callPaymentSystem(
            @AuthenticationPrincipal AuthMember authMember,
            Model model, @RequestParam Long amount) {
        model.addAttribute("memberId", authMember.getId());
        model.addAttribute("paymentAmount", amount);
        return "/payment_process";
    }

    @GetMapping("/success/{memberId}")
    public String paymentRequest(
            @PathVariable Long memberId, @RequestParam String orderId, @RequestParam Long amount) {
        // memberId, orderId, amount는 결제 요청과 승인 사이에 데이터 무결성을 확인하기 위해 필요하므로 서버에 임시 저장
        tempPaymentService.createTempPayment(memberId, orderId, amount);

        return "/payment_success";
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPayment(
            @AuthenticationPrincipal AuthMember authMember,
            @RequestBody String jsonBody) throws Exception {
        // 결제 승인 정보를 결제 내역 DB에 저장
        paymentProcessService.paymentProcess(jsonBody, authMember.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/fail")
    public String failPayment(HttpServletRequest request, Model model) {
        String failStatus = request.getParameter("status");
        String failCode = request.getParameter("code");
        String failMessage = request.getParameter("message");

        model.addAttribute("status", failStatus);
        model.addAttribute("code", failCode);
        model.addAttribute("message", failMessage);

        return "/payment_fail";
    }
}
