package org.team1.keyduck.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.util.ErrorCode;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.payment.entity.SaleProfit;
import org.team1.keyduck.payment.repository.SaleProfitRepository;

@Service
@RequiredArgsConstructor
public class SaleProfitService {

    private final SaleProfitRepository saleProfitRepository;
    private final AuctionRepository auctionRepository;

    @Transactional
    public void saleProfit(Long auctionId) {

        Auction findAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_AUCTION,
                        ErrorMessageParameter.AUCTION));

        Member seller = findAuction.getKeyboard().getMember();

        Long winningPrice = findAuction.getCurrentPrice();

        SaleProfit saleProfit = SaleProfit.builder()
                .member(seller)
                .auction(findAuction)
                .winningPrice(winningPrice)
                .build();

        saleProfitRepository.save(saleProfit);
    }
}
