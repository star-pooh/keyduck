package org.team1.keyduck.bidding.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.team1.keyduck.bidding.entity.Bidding;

public interface BiddingRepository extends JpaRepository<Bidding, Long> {

    //특정유저의 특정경매 당 비딩 횟수
    long countByMember_IdAndAuction_Id(Long memberId, Long auctionId);

    @Query("SELECT t FROM Bidding t WHERE t.auction.id = :auctionId AND t.price = "
            + "(SELECT MAX(b.price) FROM Bidding b WHERE b.auction.id = :auctionId AND b.member.id = t.member.id)"
            + "AND t.price < (SELECT MAX(b.price) FROM Bidding b WHERE b.auction.id = :auctionId)")
    List<Bidding> findAllByIdBiddingMax(Long auctionId);

    @Query("SELECT b FROM Bidding b WHERE b.price = "
            + "(SELECT MAX(b.price) FROM Bidding b WHERE b.auction.id = :auctionId)")
    Bidding findByMaxPriceAuctionId(Long auctionId);

    //경매별 입찰내역
    List<Bidding> findByAuctionIdOrderByPriceDesc(Long auctionId);

    List<Bidding> findAllByAuctionId(Long auctionId);


    @Query("SELECT b FROM Bidding b WHERE b.price ="
            + "(SELECT MAX(b.price) FROM Bidding b WHERE b.auction.id = :auctionId AND b.member.id = :memberId)")
    Bidding findByMember_IdAndAuction_Id(Long memberId, Long auctionId);
}
