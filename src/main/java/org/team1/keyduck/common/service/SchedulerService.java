package org.team1.keyduck.common.service;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionQueryDslRepository;
import org.team1.keyduck.auction.service.AuctionService;
import org.team1.keyduck.member.entity.Member;

@Service
@Slf4j
public class SchedulerService {

    private final AuctionQueryDslRepository auctionQueryDslRepository;
    private final AuctionService auctionService;
    private final EntityManager entityManager;


    public SchedulerService(
            @Qualifier("auctionQueryDslRepositoryImpl") AuctionQueryDslRepository auctionQueryDslRepository,
            AuctionService auctionService, EntityManager entityManager) {
        this.auctionQueryDslRepository = auctionQueryDslRepository;
        this.auctionService = auctionService;
        this.entityManager = entityManager;
    }

    @Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
    @Transactional
    public void auctionOpen() {
        List<Auction> startTargetAuctionList =
                auctionQueryDslRepository.findStartTargetAuction(LocalDateTime.now());

        if (startTargetAuctionList.isEmpty()) {
            log.info("start target auction list is empty");
            return;
        }

        for (Auction startTargetAuction : startTargetAuctionList) {
            try {
                auctionService.openAuction(startTargetAuction);
                entityManager.flush();

                log.info("auctionId : {}, auctionStatus : {}", startTargetAuction.getId(), startTargetAuction.getAuctionStatus());
            } catch (Exception e) {
                log.error("auctionId : {}, auctionTitle : {} status change failed",
                        startTargetAuction.getId(), startTargetAuction.getTitle(), e);
                throw e;
            }
        }
    }

    @Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
    @Transactional
    public void auctionClose() {
        List<Auction> endTargetAuctionList =
                auctionQueryDslRepository.findEndTargetAuction(LocalDateTime.now());

        if (endTargetAuctionList.isEmpty()) {
            log.info("end target auction list is empty");
            return;
        }

        for (Auction endTargetAuction : endTargetAuctionList) {
            try {
                auctionService.closeAuction(endTargetAuction);
                entityManager.flush();

                log.info("auctionId : {}, auctionStatus : {}", endTargetAuction.getId(), endTargetAuction.getAuctionStatus());

            } catch (Exception e) {
                log.error("auctionId : {}, auctionTitle : {} close failed",
                        endTargetAuction.getId(), endTargetAuction.getTitle(), e);
                throw e;
            }
        }
    }

}