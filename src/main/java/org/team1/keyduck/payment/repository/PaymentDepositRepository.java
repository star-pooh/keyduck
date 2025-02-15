package org.team1.keyduck.payment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.keyduck.payment.entity.PaymentDeposit;

public interface PaymentDepositRepository extends JpaRepository<PaymentDeposit, Long> {

    Optional<PaymentDeposit> findByMember_Id(Long memberId);
}
