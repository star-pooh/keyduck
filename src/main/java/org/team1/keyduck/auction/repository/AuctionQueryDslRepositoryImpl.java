package org.team1.keyduck.auction.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.team1.keyduck.auction.dto.response.AuctionReadAllResponseDto;

@Repository
@RequiredArgsConstructor
public class AuctionQueryDslRepositoryImpl implements AuctionQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final AuctionRepository auctionRepository;

    @Override
    public Page<AuctionReadAllResponseDto> findAllAuction(Pageable pageable, String option) {

        return null;
    }

}

