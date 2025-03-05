package org.team1.keyduck.auction.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION1;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.team1.keyduck.auction.dto.response.AuctionReadResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionSearchResponseDto;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
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
    void findAuctionSuccess() {
        // given
        Auction auction = TEST_AUCTION1;

        when(auctionRepository.findById(TEST_AUCTION_ID1)).thenReturn(Optional.of(auction));
        when(biddingRepository.findAllByAuctionId(TEST_AUCTION_ID1)).thenReturn(TEST_BIDDINGS);

        // List<Bidding> → List<BiddingResponseDto> 변환
        List<BiddingResponseDto> responseDtos = TEST_BIDDINGS.stream()
                .map(BiddingResponseDto::of)
                .toList();

        AuctionReadResponseDto expected = AuctionReadResponseDto.of(auction, responseDtos);

        // when
        AuctionReadResponseDto actual = auctionService.findAuction(TEST_AUCTION_ID1);

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("biddings")
                .isEqualTo(expected);

        assertEquals(2, actual.getBiddings().size());
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
        Pageable pageable = PageRequest.of(0, 10);

        AuctionSearchResponseDto mockDto1 = mock(AuctionSearchResponseDto.class);
        AuctionSearchResponseDto mockDto2 = mock(AuctionSearchResponseDto.class);
        AuctionSearchResponseDto mockDto3 = mock(AuctionSearchResponseDto.class);

        List<AuctionSearchResponseDto> mockResults = List.of(mockDto1, mockDto2, mockDto3);

        Page<AuctionSearchResponseDto> mockPage = new PageImpl<>(mockResults, pageable, mockResults.size());

        when(auctionRepository.findAllAuction(pageable, null, null, null)).thenReturn(mockPage);

        // when
        Page<AuctionSearchResponseDto> result = auctionService.findAllAuction(pageable, null, null, null);

        // then
        assertEquals(3, result.getTotalElements());
    }

    @Test
    @DisplayName("경매 다건 조회 - 성공 케이스(존재하지 않는 경매)")
    void findAllAuctionFailEmpty() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        Page<AuctionSearchResponseDto> responseDto = new PageImpl<>(List.of(), pageable, 0);
        when(auctionRepository.findAllAuction(pageable, null, null, null)).thenReturn(responseDto);

        // when
        Page<AuctionSearchResponseDto> result = auctionService.findAllAuction(pageable, null, null, null);

        // then
        assertTrue(result.isEmpty());
    }
}