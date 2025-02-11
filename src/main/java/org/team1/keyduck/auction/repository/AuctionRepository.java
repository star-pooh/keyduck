package org.team1.keyduck.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.keyduck.auction.entity.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

}
