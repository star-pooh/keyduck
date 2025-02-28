package org.team1.keyduck.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.payment.entity.SaleProfit;
import org.team1.keyduck.payment.repository.SaleProfitRepository;
import org.team1.keyduck.payment.service.SaleProfitService;

@ExtendWith(MockitoExtension.class)
public class SaleProfitServiceTest {

    @Mock
    SaleProfitRepository saleProfitRepository;

    @Mock
    AuctionRepository auctionRepository;

    @InjectMocks
    SaleProfitService saleProfitService;

    @Test
    @DisplayName("성공_종료된 경매의 낙찰가를 판매자에게 지급")
    public void successSaleProfit() {
        // given
        Long auctionId = 1L;
        Auction findAuction = mock(Auction.class);
        Keyboard findKeyboard = mock(Keyboard.class);
        Member seller = mock(Member.class);
        Long winningPrice = 10000L;
        SaleProfit saleProfit = mock(SaleProfit.class);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(findAuction));
        when(findAuction.getKeyboard()).thenReturn(findKeyboard);
        when(findKeyboard.getMember()).thenReturn(seller);
        when(findAuction.getCurrentPrice()).thenReturn(winningPrice);
        when(saleProfitRepository.save(any(SaleProfit.class))).thenReturn(saleProfit);

        // when
        saleProfitService.saleProfit(auctionId);

        // then
        verify(saleProfitRepository, times(1)).save(any(SaleProfit.class));
    }

    @Test
    @DisplayName("실패_경매에 대한 정보를 찾을 수 없음")
    public void failSaleProfitWithNotFoundAuction() {
        // given
        Long auctionId = 1L;

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.empty());

        // when
        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> saleProfitService.saleProfit(auctionId));

        // then
        assertThat("해당 경매을(를) 찾을 수 없습니다.").isEqualTo(exception.getMessage());
    }
}
