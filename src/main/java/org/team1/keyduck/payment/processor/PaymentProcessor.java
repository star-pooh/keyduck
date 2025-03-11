package org.team1.keyduck.payment.processor;

import java.util.UUID;
import org.json.simple.JSONObject;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.payment.entity.Payment;

public interface PaymentProcessor {

    JSONObject parseJsonBody(String jsonBody);

    String createAuthorization();

    JSONObject approvalPaymentRequest(JSONObject jsonObject, UUID idempotencyKey) throws Exception;

    Payment getCreatePaymentData(JSONObject jsonObject, Member member);

//    Payment getConfirmPaymentData(JSONObject jsonObject);

    JSONObject cancelPaymentRequest(String paymentKey, UUID idempotencyKey)
            throws Exception;

    Payment getPaymentData(JSONObject jsonObject, boolean isCancelRequest);
}
