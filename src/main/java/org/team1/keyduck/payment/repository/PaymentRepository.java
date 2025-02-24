package org.team1.keyduck.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.keyduck.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
