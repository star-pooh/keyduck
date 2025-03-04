package org.team1.keyduck.auction.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.team1.keyduck.auction.dto.request.AuctionUpdateRequestDto;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataUnauthorizedAccessException;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.testdata.TestData;

@ExtendWith(MockitoExtension.class)
class AuctionUpdateServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private AuctionService auctionService;

    @Test
    @DisplayName("경매 수정 _성공")
    public void auctionUpdateSuccess() {

        Member member = mock(Member.class);
        Keyboard keyboard = mock(Keyboard.class);

        Auction auction = Auction.builder()
                .keyboard(keyboard)
                .member(null)
                .title(TestData.TEST_AUCTION_TITLE)
                .startPrice(TestData.TEST_AUCTION_START_PRICE1)
                .immediatePurchasePrice(TestData.TEST_AUCTION_IMMEDIATE_PURCHASE_PRICE1)
                .currentPrice(TestData.TEST_AUCTION_START_PRICE1)
                .biddingUnit(TestData.TEST_AUCTION_BIDDING_UNIT1)
                .auctionStartDate(LocalDateTime.now().plusDays(1))
                .auctionEndDate(LocalDateTime.now().plusDays(3))
                .auctionStatus(AuctionStatus.NOT_STARTED)
                .build();

        AuctionUpdateRequestDto requestDto = new AuctionUpdateRequestDto();

        ReflectionTestUtils.setField(requestDto, "title", "제목 변경");

        when(auctionRepository.findById(auction.getId())).thenReturn(Optional.of(auction));
        when(auction.getKeyboard().getMember()).thenReturn(member);
        when(auction.getKeyboard().getMember().getId()).thenReturn(1L);

        auctionService.auctionModification(member.getId(), auction.getId(), requestDto);

        assertEquals(auction.getTitle(), "제목 변경");
    }

    @Test
    @DisplayName("경매 수정_실패_경매_없음")
    public void auctionUpdateFailNotFoundAuction() {

        Long auctionId = 1L;
        Long sellerId = 1L;

        AuctionUpdateRequestDto requestDto = mock(AuctionUpdateRequestDto.class);

        ReflectionTestUtils.setField(requestDto, "title", "제목 변경");

        when(auctionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        DataNotFoundException e = assertThrows(DataNotFoundException.class,
                () -> auctionService.auctionModification(
                        sellerId, auctionId, requestDto));

        assertEquals("해당 경매을(를) 찾을 수 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("경매 수정_실패_경매_진행중")
    public void auctionUpdateFailAuctionInProgress() {

        Auction auction = mock(Auction.class);

        Long sellerId = 1L;

        AuctionUpdateRequestDto requestDto = mock(AuctionUpdateRequestDto.class);

        ReflectionTestUtils.setField(requestDto, "title", "제목 변경");
        ReflectionTestUtils.setField(auction, "id", 1L);

        when(auctionRepository.findById(any(Long.class))).thenReturn(Optional.of(auction));
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.IN_PROGRESS);

        DataInvalidException e = assertThrows(DataInvalidException.class,
                () -> auctionService.auctionModification(
                        sellerId, auction.getId(), requestDto));

        assertEquals("유효하지 않은 경매 상태 입니다.", e.getMessage());
    }

    @Test
    @DisplayName("경매 수정_실패_경매를 등록한 셀러가 아님")
    public void auctionUpdateFailUnauthorized() {

        Auction auction = mock(Auction.class);
        Member member = mock(Member.class);
        Keyboard keyboard = mock(Keyboard.class);
        AuctionUpdateRequestDto requestDto = mock(AuctionUpdateRequestDto.class);

        ReflectionTestUtils.setField(auction, "id", 1L);

        when(auctionRepository.findById(auction.getId())).thenReturn(Optional.of(auction));
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.NOT_STARTED);

        when(auction.getKeyboard()).thenReturn(keyboard);
        when(keyboard.getMember()).thenReturn(member);
        when(auction.getKeyboard().getMember().getId()).thenReturn(TestData.TEST_ID1);

        DataUnauthorizedAccessException e = assertThrows(
                DataUnauthorizedAccessException.class,
                () -> auctionService.auctionModification(TestData.TEST_ID2,
                        auction.getId(), requestDto));

        assertEquals("접근 권한이 없습니다.", e.getMessage());
    }
}