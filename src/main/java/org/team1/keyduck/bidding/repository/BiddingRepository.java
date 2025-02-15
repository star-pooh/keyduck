package org.team1.keyduck.bidding.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.team1.keyduck.bidding.entity.Bidding;

public interface BiddingRepository extends JpaRepository<Bidding, Long> {

    //특정유저의 특정경매 당 비딩 횟수
    long countByMember_IdAndAuction_Id(Long memberId, Long auctionId);

    //경매별 입찰내역
    List<Bidding> findByAuctionIdOrderByPriceDesc(Long auctionId);

    @Query("SELECT b FROM Bidding b WHERE b.auction.id IN :auctionIds AND b.price = "
        + "(SELECT MAX(b2.price) FROM Bidding b2 WHERE b2.auction.id = b.auction.id)")
    List<Bidding> findSuccessBiddingByAuctionIds(List<Long> auctionIds, Pageable pageable);
}
