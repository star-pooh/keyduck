package org.team1.keyduck.payment.processor;

import org.json.simple.JSONObject;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.payment.entity.Payment;

public interface PaymentProcessor {

    JSONObject parseJsonBody(String jsonBody);

    String createAuthorization();

    JSONObject requestPaymentApproval(JSONObject jsonObject) throws Exception;

    Payment createPaymentData(JSONObject jsonObject, Member member);
}
