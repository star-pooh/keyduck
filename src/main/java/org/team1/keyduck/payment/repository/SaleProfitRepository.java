package org.team1.keyduck.payment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.team1.keyduck.payment.entity.SaleProfit;

public interface SaleProfitRepository extends JpaRepository<SaleProfit, Long> {

    @Query("SELECT SUM (p.winningPrice) FROM SaleProfit p WHERE p.member.id = :memberId")
    Optional<Long> findSellerPointByMember_Id(Long memberId);

}
