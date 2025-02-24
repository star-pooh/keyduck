package org.team1.keyduck.payment.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.team1.keyduck.payment.entity.TempPayment;
import org.team1.keyduck.payment.repository.TempPaymentRepository;
import org.team1.keyduck.testdata.TestData;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TempPaymentServiceTest {

    @Autowired
    TempPaymentRepository tempPaymentRepository;

    @Test
    @DisplayName("임시 결제 내역 저장 성공")
    public void successTempPaymentSave() {
        // given
        TempPayment tempPayment = TestData.TEST_TEMP_PAYMENT1;

        // when
        TempPayment actualTempPayment = tempPaymentRepository.save(tempPayment);

        // then
        TempPayment expectedTempPayment = TempPayment.builder()
                .memberId(1L)
                .orderId("orderId1")
                .amount(1000L)
                .build();

        assertThat(actualTempPayment).usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringFields("cratedAt")
                .ignoringFields("modifiedAt")
                .isEqualTo(expectedTempPayment);
    }
}