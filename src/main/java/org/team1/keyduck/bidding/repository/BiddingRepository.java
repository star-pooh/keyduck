package org.team1.keyduck.bidding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.member.entity.Member;

public interface BiddingRepository extends JpaRepository<Bidding, Long> {
    //특정유저의 특정경매 당 비딩 횟수
    long countByMember_IdAndAuction_Id(Long memberId, Long auctionId);
}
