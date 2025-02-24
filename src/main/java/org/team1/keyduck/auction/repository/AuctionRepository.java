package org.team1.keyduck.auction.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("SELECT t FROM Auction t WHERE t.member.id = :memberId AND t.auctionStatus = :auctionStatus")
    List<Auction> findAllByMember_IdAndAuctionStatus(Long memberId, AuctionStatus auctionStatus);

    boolean existsByKeyboard_Member_IdAndAuctionStatus(Long id, AuctionStatus auctionStatus);

    List<Auction> findAllByOrderByIdDesc();
}
