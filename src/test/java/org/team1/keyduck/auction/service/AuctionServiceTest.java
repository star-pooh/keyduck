package org.team1.keyduck.auction.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION1;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION2;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_BIDDINGS;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.keyduck.auction.dto.response.AuctionReadAllResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionReadResponseDto;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataNotFoundException;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private BiddingRepository biddingRepository;

    @InjectMocks
    private AuctionService auctionService;

    @Test
    @DisplayName("경매 단건 조회 - 성공 케이스")
    void findAuctionSuccessWithBiddings() {
        // given
        Auction auction = TEST_AUCTION1;

        when(auctionRepository.findById(TEST_AUCTION_ID1)).thenReturn(Optional.of(auction));

        when(biddingRepository.findAllByAuctionId(TEST_AUCTION_ID1)).thenReturn(TEST_BIDDINGS);

        // when
        AuctionReadResponseDto result = auctionService.findAuction(TEST_AUCTION_ID1);

        // then
        assertEquals(auction.getTitle(), result.getTitle());
        assertEquals(auction.getStartPrice(), result.getStartPrice());
        assertEquals(auction.getCurrentPrice(), result.getCurrentPrice());
        assertEquals(auction.getImmediatePurchasePrice(), result.getImmediatePurchasePrice());
        assertEquals(auction.getBiddingUnit(), result.getBiddingUnit());
        assertEquals(auction.getAuctionStartDate(), result.getAuctionStartDate());
        assertEquals(auction.getAuctionEndDate(), result.getAuctionEndDate());
        assertEquals(auction.getAuctionStatus(), result.getAuctionStatus());
        assertEquals(2, result.getBiddings().size());
    }

    @Test
    @DisplayName("경매 단건 조회 - 실패 케이스 (존재하지 않는 경매)")
    void findAuctionFailEmpty() {
        // given
        Long auctionId = 3L; //존재하지 않는 경매 ID 설정

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.empty());

        // when
        // then
        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> auctionService.findAuction(auctionId));

        assertEquals("해당 경매을(를) 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("경매 다건 조회 - 성공 케이스")
    void findAllAuction_success() {
        // given
        List<Auction> auctions = List.of(TEST_AUCTION1, TEST_AUCTION2);

        when(auctionRepository.findAllByOrderByIdDesc()).thenReturn(auctions);

        // when
        List<AuctionReadAllResponseDto> result = auctionService.findAllAuction();

        // then
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("경매 다건 조회 - 실패 케이스(존재하지 않는 경매)")
    void findAllAuction_fail_empty() {
        // given
        when(auctionRepository.findAllByOrderByIdDesc()).thenReturn(List.of());

        // when
        List<AuctionReadAllResponseDto> result = auctionService.findAllAuction();

        // then
        assertTrue(result.isEmpty());
    }

}