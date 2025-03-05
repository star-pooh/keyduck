package org.team1.keyduck.common.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionQueryDslRepository;

@Service
@Slf4j
public class SchedulerService {

    private final AuctionQueryDslRepository auctionQueryDslRepository;

    public SchedulerService(
            @Qualifier("auctionQueryDslRepositoryImpl") AuctionQueryDslRepository auctionQueryDslRepository) {
        this.auctionQueryDslRepository = auctionQueryDslRepository;
    }

    @Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
    @Transactional
    public void auctionStart() {
        List<Auction> startTargetAuctionList =
                auctionQueryDslRepository.findStartTargetAuction(LocalDateTime.now());

        if (startTargetAuctionList.isEmpty()) {
            log.info("start target auction list is empty");
        }

        for (Auction startTargetAuction : startTargetAuctionList) {
            startTargetAuction.updateAuctionStatus(AuctionStatus.IN_PROGRESS);
            log.info("auctionId : {}, auctionTitle : {}, in progress status change success",
                    startTargetAuction.getId(), startTargetAuction.getTitle());
        }
    }
}
