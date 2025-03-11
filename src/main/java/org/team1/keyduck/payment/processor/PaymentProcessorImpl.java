package org.team1.keyduck.payment.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.payment.entity.Payment;
import org.team1.keyduck.payment.util.PaymentMethod;
import org.team1.keyduck.payment.util.PaymentStatus;

@Component
public class PaymentProcessorImpl implements PaymentProcessor {

    @Value("${payment.toss.test-secret-api-key}")
    private String secretKey;

    @Value("${payment.toss.payment-confirm-url}")
    private String paymentConfirmUrl;

    @Value("${payment.toss.payment-cancel-url}")
    private String paymentCancelUrl;

    private static final JSONParser PARSER = new JSONParser();

    /**
     * JSON 데이터 파싱
     *
     * @param jsonBody 클라이언트에게 받은 결제 정보 데이터
     * @return JSON 형태로 변환된 결제 정보 데이터
     */
    @Override
    public JSONObject parseJsonBody(String jsonBody) {
        try {
            return (JSONObject) PARSER.parse(jsonBody);
        } catch (ParseException e) {
            throw new DataInvalidException(ErrorCode.INVALID_DATA_TYPE,
                    ErrorMessageParameter.JSON_TYPE);
        }
    }

    /**
     * Basic 인증 헤더 설정
     *
     * @return Basic 인증 헤더
     */
    @Override
    public String createAuthorization() {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedBytes);
    }

    /**
     * 결제 승인 API 호출 (서버 -> 토스페이먼츠)
     *
     * @param jsonObject     JSON 형태로 변환된 결제 정보 데이터
     * @param idempotencyKey 멱등키
     * @return 토스페이먼츠에서 보내준 payment 객체
     * @throws Exception exception
     */
    @Override
    public JSONObject approvalPaymentRequest(JSONObject jsonObject, UUID idempotencyKey)
            throws Exception {
        return executeHttpUrlConnection(paymentConfirmUrl, jsonObject, idempotencyKey);
    }

    /**
     * 결제 내역 DB에 저장하기 위한 Payment 데이터 생성
     *
     * @param jsonObject JSON 형태로 변환된 결제 정보 데이터
     * @param member     결제 요청 멤버 정보
     * @return 결제 내역 DB에 저장하기 위한 Payment 데이터
     */
    @Override
    public Payment getCreatePaymentData(JSONObject jsonObject, Member member) {
        String paymentKey = (String) jsonObject.get("paymentKey");
        String orderId = (String) jsonObject.get("orderId");
        String amount = (String) jsonObject.get("amount");

        return Payment.builder()
                .member(member)
                .paymentKey(paymentKey)
                .orderId(orderId)
                .amount(Long.valueOf(amount))
                .build();
    }

    /**
     * 결제 내역 DB를 갱신하기 위한 Payment 데이터 생성
     *
     * @param jsonObject 토스페이먼츠에서 보내준 payment 객체 정보
     * @return 결제 내역 DB를 갱신하기 위한 Payment 데이터
     */
    @Override
    public Payment getConfirmPaymentData(JSONObject jsonObject) {
        JSONObject easyPay = (JSONObject) jsonObject.get("easyPay");

        String paymentMethod = (String) jsonObject.get("method");
        String easyPayType = easyPay != null ? easyPay.get("provider").toString() : null;
        String paymentStatus = (String) jsonObject.get("status");
        String requestedAt = (String) jsonObject.get("requestedAt");
        String approvedAt = (String) jsonObject.get("approvedAt");

        return Payment.builder()
                .paymentMethod(PaymentMethod.getPaymentType(paymentMethod))
                .easyPayType(easyPayType)
                .paymentStatus(PaymentStatus.valueOf(paymentStatus))
                .requestedAt(
                        LocalDateTime.parse(requestedAt.substring(0, requestedAt.indexOf("+"))))
                .approvedAt(LocalDateTime.parse(approvedAt.substring(0, approvedAt.indexOf("+"))))
                .build();
    }

    @Override
    public JSONObject cancelPaymentRequest(String paymentKey, UUID idempotencyKey)
            throws Exception {
        String cancelUrl = paymentCancelUrl.replace("{paymentKey}", paymentKey);
        JSONObject jsonObject = createCancelRequestJsonObject();

        return executeHttpUrlConnection(cancelUrl, jsonObject, idempotencyKey);
    }

    private JSONObject createCancelRequestJsonObject() throws ParseException {
        JSONParser parser = new JSONParser();
        String cancelReasonJsonBody = "{\"cancelReason\":\"server error\"}";

        return (JSONObject) parser.parse(cancelReasonJsonBody);
    }

    private JSONObject executeHttpUrlConnection(String strUrl, JSONObject jsonObject,
            UUID idempotencyKey) throws IOException, ParseException {
        String authorization = createAuthorization();

        URL url = new URL(strUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Idempotency-Key", idempotencyKey.toString());
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        }

        try (InputStream responseStream =
                connection.getResponseCode() == HttpStatus.OK.value()
                        ? connection.getInputStream()
                        : connection.getErrorStream();
                Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            return (JSONObject) PARSER.parse(reader);
        }
    }
}
