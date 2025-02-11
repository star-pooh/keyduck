package org.team1.keyduck.bidding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.keyduck.bidding.entity.Bidding;

public interface BiddingRepository extends JpaRepository<Bidding, Long> {

}
