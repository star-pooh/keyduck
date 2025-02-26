package org.team1.keyduck.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.dto.PaymentDto;
import org.team1.keyduck.payment.entity.Payment;
import org.team1.keyduck.payment.processor.PaymentProcessor;
import org.team1.keyduck.payment.repository.PaymentRepository;
import org.team1.keyduck.testdata.TestData;

@ExtendWith(MockitoExtension.class)
@RestClientTest(value = PaymentService.class)
@MockitoBean(types = {JpaMetamodelMappingContext.class})
public class PaymentServiceTest {

    @MockitoBean
    PaymentRepository paymentRepository;

    @MockitoBean
    MemberRepository memberRepository;

    @MockitoBean
    PaymentProcessor paymentProcessor;

    @MockitoBean
    TempPaymentService tempPaymentService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Autowired
    MockRestServiceServer mockServer;


    @BeforeEach
    public void setup() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("결제 승인 및 결제 내역 저장 성공")
    public void successPaymentApproveAndPaymentSave() throws Exception {

        // given
        JSONParser parser = new JSONParser();

        // 구매자가 보낸 결제 요청 정보
        String paymentRequestJsonBody = "{\"paymentKey\":\"test_paymentKey\""
                + ",\"orderId\":\"orderId1\""
                + ",\"amount\":\"1000\"}";
        JSONObject paymentRequestJsonObject = (JSONObject) parser.parse(paymentRequestJsonBody);

        // 토스 서버로부터 받은 응답값
        String tossServerUrl = "https://api.tosspayments.com/v1/payments/confirm";
        String tossResponseJsonBody =
                "{\"orderId\":\"orderId1\""
                        + "\"totalAmount\":\"1000\""
                        + "\"method\":\"간편결제\""
                        + "\"easyPay\":\"{\\\"provider\\\":\\\"카카오페이\\\"}\","
                        + "\"status\":\"DONE\""
                        + "\"requestedAt\":\"2025-02-24T22:52:37+09:00\""
                        + "\"approvedAt\":\"2025-02-24T22:55:44+09:00\"}";
        JSONObject tossServerResponseJsonObject = (JSONObject) parser.parse(tossResponseJsonBody);

        Member member = mock(Member.class);

        // 토스 서버로 요청을 보내기 전에 결제 금액이 조작되었는지 확인
        when(memberRepository.findById(any(Long.class))).thenReturn(
                Optional.ofNullable(member));
        when(paymentProcessor.parseJsonBody(any(String.class))).thenReturn(
                paymentRequestJsonObject);
        when(tempPaymentService.validPaymentAmount(any(Long.class), any(String.class),
                any(Long.class))).thenReturn(true);

        // 토스 서버에 승인 요청을 보내고 받은 응답값
        InputStreamResource tossResponseResource = new InputStreamResource(
                new ByteArrayInputStream(tossResponseJsonBody.getBytes(StandardCharsets.UTF_8)));
        mockServer.expect(requestTo(tossServerUrl))
                .andRespond(withSuccess(tossResponseResource, MediaType.APPLICATION_JSON));
        when(paymentProcessor.requestPaymentApproval(any(JSONObject.class))).thenReturn(
                tossServerResponseJsonObject);

        // 승인 요청에 대한 응답값을 기반으로 결제 내역 테이블에 데이터를 저장
        when(paymentProcessor.createPaymentData(any(JSONObject.class),
                any(Member.class))).thenReturn(TestData.TEST_PAYMENT1);
        when(paymentRepository.save(any(Payment.class))).thenReturn(TestData.TEST_PAYMENT1);

        PaymentDto expectPaymentDto = PaymentDto.of(TestData.TEST_PAYMENT1);

        // when
        PaymentDto actualPaymentDto = paymentService.createPayment(paymentRequestJsonBody,
                TestData.TEST_ID1);

        // then
        assertThat(actualPaymentDto).usingRecursiveComparison().isEqualTo(expectPaymentDto);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("결제 승인 실패_존재하지 않는 멤버")
    public void failPaymentApproveWithNotFoundMember() {

        // given
        String paymentRequestJsonBody = "testData";

        // when & then
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            paymentService.createPayment(paymentRequestJsonBody, TestData.TEST_ID1);
        });

        assertEquals("해당 멤버을(를) 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("결제 승인 실패_결제 금액이 조작된 경우")
    public void failPaymentApproveWithNotMatchPaymentAmount222() throws Exception {

        // given
        JSONParser parser = new JSONParser();

        // 구매자가 보낸 결제 요청 정보
        String paymentRequestJsonBody = "{\"paymentKey\":\"test_paymentKey\""
                + ",\"orderId\":\"orderId1\""
                + ",\"amount\":\"1000\"}";
        JSONObject paymentRequestJsonObject = (JSONObject) parser.parse(paymentRequestJsonBody);

        Member member = mock(Member.class);

        // 토스 서버로 요청을 보내기 전에 결제 금액이 조작되었는지 확인
        when(memberRepository.findById(any(Long.class))).thenReturn(
                Optional.ofNullable(member));
        when(paymentProcessor.parseJsonBody(any(String.class))).thenReturn(
                paymentRequestJsonObject);
        when(tempPaymentService.validPaymentAmount(any(Long.class), any(String.class),
                any(Long.class))).thenReturn(false);

        // when & then
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            paymentService.createPayment(paymentRequestJsonBody, TestData.TEST_ID1);
        });

        assertEquals("유효하지 않은 결제 금액 입니다.", exception.getMessage());
    }
}
