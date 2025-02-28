package org.team1.keyduck.auction.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.team1.keyduck.auction.dto.request.AuctionUpdateRequestDto;
import org.team1.keyduck.auction.dto.response.AuctionUpdateResponseDto;
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
    public void auction_update_success() {

        Member member = mock(Member.class);
        Auction auction = mock(Auction.class);
        Keyboard keyboard = mock(Keyboard.class);

        AuctionUpdateRequestDto requestDto = new AuctionUpdateRequestDto();

        ReflectionTestUtils.setField(requestDto, "title", TestData.TEST_AUCTION_TITLE2);

        when(auctionRepository.findById(auction.getId())).thenReturn(Optional.of(auction));
        when(auction.getKeyboard()).thenReturn(keyboard);
        when(keyboard.getMember()).thenReturn(member);
        when(auction.getKeyboard().getMember().getId()).thenReturn(1L);
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.NOT_STARTED);

        AuctionUpdateResponseDto responseDto = auctionService.auctionModification(
                member.getId(), auction.getId(), requestDto);

        assertNotNull(requestDto);
        assertEquals(requestDto.getTitle(), TestData.TEST_AUCTION_TITLE2);
    }

    @Test
    @DisplayName("경매 수정_실패_경매_없음")
    public void auction_update_fail_not_found_auction() {

        Auction auction = mock(Auction.class);
        Member member = mock(Member.class);
        AuctionUpdateRequestDto requestDto = new AuctionUpdateRequestDto();

        when(auctionRepository.findById(auction.getId())).thenReturn(Optional.empty());

        DataNotFoundException e = assertThrows(DataNotFoundException.class,
                () -> auctionService.auctionModification(
                        member.getId(), auction.getId(), requestDto));

        assertEquals("해당 경매을(를) 찾을 수 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("경매 수정_실패_경매_진행중")
    public void auction_update_fail_auction_in_progress() {

        Auction auction = mock(Auction.class);
        Member member = mock(Member.class);
        AuctionUpdateRequestDto requestDto = new AuctionUpdateRequestDto();

        when(auctionRepository.findById(auction.getId())).thenReturn(Optional.of(auction));
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.IN_PROGRESS);

        DataInvalidException e = assertThrows(DataInvalidException.class,
                () -> auctionService.auctionModification(
                        member.getId(), auction.getId(), requestDto));

        assertEquals("유효하지 않은 경매 상태 입니다.", e.getMessage());
    }

    @Test
    @DisplayName("경매 수정_실패_경매를 등록한 셀러가 아님")
    public void auction_update_fail_Unauthorized() {

        Auction auction = mock(Auction.class);
        Member member = mock(Member.class);
        Keyboard keyboard = mock(Keyboard.class);
        AuctionUpdateRequestDto requestDto = new AuctionUpdateRequestDto();

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