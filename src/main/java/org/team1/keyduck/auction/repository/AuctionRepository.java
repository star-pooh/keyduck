package org.team1.keyduck.auction.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("SELECT t FROM Auction t WHERE t.member.id = :memberId AND t.auctionStatus = :auctionStatus")
    List<Auction> findAllByMember_IdAndAuctionStatus(Long memberId, AuctionStatus auctionStatus);

    boolean existsByKeyboard_Member_IdAndAuctionStatus(Long id, AuctionStatus auctionStatus);

    boolean existsByKeyboard_Id(Long keyboardId);

    @Query("SELECT COUNT(a) > 0 FROM Auction a WHERE a.keyboard.id = :keyboardId AND a.auctionStatus IN :statuses")
    boolean existsByKeyboard_IdAndAuctionStatus(Long keyboardId,
            List<AuctionStatus> statuses);

    // 비관적 락 적용
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Auction a WHERE a.id = :auctionId")
    Optional<Auction> findByIdWithPessimisticLock(@Param("auctionId") Long auctionId);

    boolean existsAuctionByKeyboardId(Long keyboardId);
}
