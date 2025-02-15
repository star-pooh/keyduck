package org.team1.keyduck.payment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.keyduck.payment.entity.TempPayment;

public interface TempPaymentRepository extends JpaRepository<TempPayment, Long> {

    Optional<TempPayment> findByOrderId(String orderId);
}
