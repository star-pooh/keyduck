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
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.payment.service.PaymentDepositService;
import org.team1.keyduck.payment.service.SaleProfitService;

@Service
@Slf4j
public class SchedulerService {

    private final AuctionQueryDslRepository auctionQueryDslRepository;
    private final BiddingRepository biddingRepository;
    private final PaymentDepositService paymentDepositService;
    private final SaleProfitService saleProfitService;


    public SchedulerService(
            @Qualifier("auctionQueryDslRepositoryImpl") AuctionQueryDslRepository auctionQueryDslRepository,
            BiddingRepository biddingRepository,
            PaymentDepositService paymentDepositService,
            SaleProfitService saleProfitService) {
        this.auctionQueryDslRepository = auctionQueryDslRepository;
        this.biddingRepository = biddingRepository;
        this.paymentDepositService = paymentDepositService;
        this.saleProfitService = saleProfitService;
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

    @Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
    @Transactional
    public void auctionEnd() {
        List<Auction> endTargetAuctionList =
                auctionQueryDslRepository.findEndTargetAuction(LocalDateTime.now());

        if (endTargetAuctionList.isEmpty()) {
            log.info("end target auction list is empty");
            return;
        }

        for (Auction endTargetAuction : endTargetAuctionList) {
            Member winner = biddingRepository.findByMaxPriceAuctionId(endTargetAuction.getId());
            endTargetAuction.updateSuccessBiddingMember(winner);

            paymentDepositService.refundPaymentDeposit(endTargetAuction.getId());
            saleProfitService.saleProfit(endTargetAuction.getId());

            endTargetAuction.updateAuctionStatus(AuctionStatus.CLOSED);

            log.info("auctionId : {}, auctionTitle : {}, closed status change success",
                    endTargetAuction.getId(), endTargetAuction.getTitle());
        }
    }
}
