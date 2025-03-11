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
        List<Auction> openTargetAuctionList =
                auctionQueryDslRepository.findOpenTargetAuction(LocalDateTime.now());

        if (openTargetAuctionList.isEmpty()) {
            log.info("start target auction list is empty");
            return;
        }

        for (Auction openTargetAuction : openTargetAuctionList) {
            try {
                auctionService.openAuction(openTargetAuction);
                entityManager.flush();

                log.info("auctionId : {}, auctionStatus : {}", openTargetAuction.getId(), openTargetAuction.getAuctionStatus());
            } catch (Exception e) {
                log.error("auctionId : {}, auctionTitle : {} status change failed",
                        openTargetAuction.getId(), openTargetAuction.getTitle(), e);
                throw e;
            }
        }
    }

    @Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
    @Transactional
    public void auctionClose() {
        List<Auction> closeTargetAuctionList =
                auctionQueryDslRepository.findCloseTargetAuction(LocalDateTime.now());

        if (closeTargetAuctionList.isEmpty()) {
            log.info("end target auction list is empty");
            return;
        }

        for (Auction closeTargetAuction : closeTargetAuctionList) {
            try {
                auctionService.closeAuction(closeTargetAuction);
                entityManager.flush();

                log.info("auctionId : {}, auctionStatus : {}", closeTargetAuction.getId(), closeTargetAuction.getAuctionStatus());

            } catch (Exception e) {
                log.error("auctionId : {}, auctionTitle : {} close failed",
                        closeTargetAuction.getId(), closeTargetAuction.getTitle(), e);
                throw e;
            }
        }
    }

}