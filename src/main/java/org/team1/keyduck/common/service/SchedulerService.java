package org.team1.keyduck.common.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.team1.keyduck.auction.repository.AuctionQueryDslRepository;
import org.team1.keyduck.auction.service.AuctionService;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {

    private final AuctionQueryDslRepository auctionQueryDslRepository;
    private final AuctionService auctionService;

    @Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
    @SchedulerLock(name = "auctionOpen", lockAtMostFor = "1h", lockAtLeastFor = "5m")
    public void auctionOpen() {
        List<Long> openTargetAuctionIdList =
                auctionQueryDslRepository.findOpenTargetAuction(LocalDateTime.now());

        if (openTargetAuctionIdList.isEmpty()) {
            log.info("open target auction list is empty");
            return;
        }

        for (Long openTargetAuctionId : openTargetAuctionIdList) {
            try {
                auctionService.openAuction(openTargetAuctionId);
            } catch (Exception e) {
                log.error("auctionId : {} - scheduler open failed", openTargetAuctionId, e);
                throw e;
            }
        }
    }

    @Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
    @SchedulerLock(name = "auctionClose", lockAtMostFor = "1h", lockAtLeastFor = "5m")
    public void auctionClose() {
        List<Long> closeTargetAuctionIdList =
                auctionQueryDslRepository.findCloseTargetAuction(LocalDateTime.now());

        if (closeTargetAuctionIdList.isEmpty()) {
            log.info("close target auction list is empty");
            return;
        }

        for (Long closeTargetAuctionId : closeTargetAuctionIdList) {
            try {
                auctionService.closeAuction(closeTargetAuctionId);
            } catch (Exception e) {
                log.error("auctionId : {} - scheduler close failed", closeTargetAuctionId, e);
                throw e;
            }
        }
    }
}