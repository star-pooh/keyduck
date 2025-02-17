package org.team1.keyduck.bidding.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.keyduck.bidding.entity.Bidding;

public interface BiddingRepository extends JpaRepository<Bidding, Long> {

    //특정유저의 특정경매 당 비딩 횟수
    long countByMember_IdAndAuction_Id(Long memberId, Long auctionId);

    //경매별 입찰내역
    List<Bidding> findByAuctionIdOrderByPriceDesc(Long auctionId);

    List<Bidding> findAllByAuctionId(Long auctionId);

}
