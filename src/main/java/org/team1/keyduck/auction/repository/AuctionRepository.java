package org.team1.keyduck.auction.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.member.entity.Member;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("SELECT t.id FROM Auction t WHERE t.member = :member AND t.auctionStatus = :auctionStatus")
    List<Long> findAllByMember_IdAndAuctionStatus(Member member, AuctionStatus auctionStatus);
}
