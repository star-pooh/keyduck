package org.team1.keyduck.auction.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION1;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION2;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION3;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION4;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_BIDDINGS;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER2;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER3;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;
import org.team1.keyduck.auction.dto.response.AuctionReadResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionSearchResponseDto;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.testdata.TestData;

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
    void findAuctionSuccess() {
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
    void findAllAuctionSuccess() {
        // given
        ReflectionTestUtils.setField(TEST_AUCTION3, "member", TEST_MEMBER2);
        ReflectionTestUtils.setField(TEST_AUCTION4, "member", TEST_MEMBER3);

        List<AuctionSearchResponseDto> auctions = List.of(
                new AuctionSearchResponseDto(
                        TEST_AUCTION3.getId(),
                        TEST_AUCTION3.getKeyboard().getId(),
                        TEST_AUCTION3.getKeyboard().getName(),
                        TEST_AUCTION3.getKeyboard().getDescription(),
                        TEST_AUCTION3.getTitle(),
                        TEST_AUCTION3.getCurrentPrice(),
                        TEST_AUCTION3.getImmediatePurchasePrice(),
                        TEST_AUCTION3.getAuctionStatus(),
                        TEST_AUCTION3.getMember() != null ? TEST_AUCTION3.getMember().getId() : null,
                        TEST_AUCTION3.getMember() != null ? TEST_AUCTION3.getMember().getName() : null
                ),
                new AuctionSearchResponseDto(
                        TEST_AUCTION4.getId(),
                        TEST_AUCTION4.getKeyboard().getId(),
                        TEST_AUCTION4.getKeyboard().getName(),
                        TEST_AUCTION4.getKeyboard().getDescription(),
                        TEST_AUCTION4.getTitle(),
                        TEST_AUCTION4.getCurrentPrice(),
                        TEST_AUCTION4.getImmediatePurchasePrice(),
                        TEST_AUCTION4.getAuctionStatus(),
                        TEST_AUCTION4.getMember() != null ? TEST_AUCTION4.getMember().getId() : null,
                        TEST_AUCTION4.getMember() != null ? TEST_AUCTION4.getMember().getName() : null
                )
        );

        Page<AuctionSearchResponseDto> responseDto = new PageImpl<>(auctions);
        when(auctionRepository.findAllAuction(null, null, null, null)).thenReturn(responseDto);

        // when
        Page<AuctionSearchResponseDto> result = auctionService.findAllAuction(null, null, null, null);

        // then
        assertEquals(2, result.getSize());
    }

    @Test
    @DisplayName("경매 다건 조회 - 실패 케이스(존재하지 않는 경매)")
    void findAllAuctionFailEmpty() {
        // given
        Page<AuctionSearchResponseDto> responseDto = new PageImpl<>(List.of());
        when(auctionRepository.findAllAuction(null, null, null, null)).thenReturn(responseDto);

        // when
        Page<AuctionSearchResponseDto> result = auctionService.findAllAuction(null, null, null, null);

        // then
        assertTrue(result.isEmpty());
    }

}