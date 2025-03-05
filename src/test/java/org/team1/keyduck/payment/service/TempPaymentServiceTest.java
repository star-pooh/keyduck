package org.team1.keyduck.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import org.team1.keyduck.payment.entity.TempPayment;
import org.team1.keyduck.payment.repository.TempPaymentRepository;
import org.team1.keyduck.testdata.TestData;

@ExtendWith(MockitoExtension.class)
public class TempPaymentServiceTest {

    @Mock
    TempPaymentRepository tempPaymentRepository;

    @InjectMocks
    TempPaymentService tempPaymentService;

    @Test
    @DisplayName("성공_임시 결제 내역 저장")
    public void successTempPaymentSave() {
        // when
        tempPaymentService.createTempPayment(TestData.TEST_ID1, TestData.TEST_ORDER_ID1,
                TestData.TEST_PAYMENT_AMOUNT1);

        // then
        verify(tempPaymentRepository, times(1)).save(any(TempPayment.class));
    }

    @Test
    @DisplayName("성공_임시 결제 내역의 금액과 실제 결제 요청의 금액이 일치하는지 확인")
    public void successValidPaymentAmount() {

        // when
        when(tempPaymentRepository.findByOrderId(any(String.class))).thenReturn(
                Optional.of(TestData.TEST_TEMP_PAYMENT1));

        // then
        boolean actualResult = tempPaymentService.validPaymentAmount(TestData.TEST_ID1,
                TestData.TEST_ORDER_ID1,
                TestData.TEST_PAYMENT_AMOUNT1);

        assertTrue(actualResult);
    }

    @Test
    @DisplayName("실패_임시 결제 내역의 금액과 실제 결제 요청의 금액이 일치하는지 검증 시 임시 결제 내역이 존재하지 않음")
    public void failValidPaymentAmountWithNotFoundTempPayment() {

        // when
        when(tempPaymentRepository.findByOrderId(any(String.class))).thenThrow(
                new DataNotFoundException(
                        ErrorCode.NOT_FOUND_TEMP_PAYMENT, ErrorMessageParameter.TEMP_PAYMENT_INFO));

        // then
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            tempPaymentService.validPaymentAmount(TestData.TEST_ID1, TestData.TEST_ORDER_ID1,
                    TestData.TEST_PAYMENT_AMOUNT1);
        });

        assertEquals("해당 결제 금액 정보을(를) 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("실패_임시 결제 내역의 멤버 정보와 실제 결제 요청의 멤버 정보가 일치하지 않음")
    public void failValidPaymentAmountNotMatchMemberId() {

        // when
        when(tempPaymentRepository.findByOrderId(any(String.class))).thenReturn(
                Optional.of(TestData.TEST_TEMP_PAYMENT1));

        // then
        boolean actualResult = tempPaymentService.validPaymentAmount(TestData.TEST_ID2,
                TestData.TEST_ORDER_ID1,
                TestData.TEST_PAYMENT_AMOUNT1);

        assertFalse(actualResult);
    }

    @Test
    @DisplayName("실패_임시 결제 내역의 금액 정보와 실제 결제 요청의 금액 정보가 일치하지 않음")
    public void failValidPaymentAmountNotMatchPaymentAmount() {

        // when
        when(tempPaymentRepository.findByOrderId(any(String.class))).thenReturn(
                Optional.of(TestData.TEST_TEMP_PAYMENT1));

        // then
        boolean actualResult = tempPaymentService.validPaymentAmount(TestData.TEST_ID1,
                TestData.TEST_ORDER_ID1,
                TestData.TEST_PAYMENT_AMOUNT2);

        assertFalse(actualResult);
    }
}