package org.team1.keyduck.auction.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.BIDDING_UNIT1;
import static org.team1.keyduck.testdata.TestData.END_DATE1;
import static org.team1.keyduck.testdata.TestData.START_DATE1;
import static org.team1.keyduck.testdata.TestData.START_PRICE1;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION1;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION_TITLE1;
import static org.team1.keyduck.testdata.TestData.TEST_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_ID2;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD_ID1;

import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.keyduck.auction.dto.request.AuctionCreateRequestDto;
import org.team1.keyduck.auction.dto.response.AuctionCreateResponseDto;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.common.exception.DataDuplicateException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataUnauthorizedAccessException;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Member;

@ExtendWith(MockitoExtension.class)
class AuctionCreateServiceTest {

    @InjectMocks
    AuctionService auctionService;

    @Mock
    AuctionRepository auctionRepository;

    @Mock
    KeyboardRepository keyboardRepository;

    @Test
    @DisplayName("경매 생성 성공")
    public void createAuction_success() {
        //given
        AuctionCreateRequestDto request = new AuctionCreateRequestDto(TEST_KEYBOARD_ID1,
                TEST_AUCTION_TITLE1, START_PRICE1, null, BIDDING_UNIT1, START_DATE1, END_DATE1);

        Keyboard keyboard = mock(Keyboard.class);
        Member member = mock(Member.class);

        AuctionCreateResponseDto expectResponse = AuctionCreateResponseDto.of(TEST_AUCTION1);

        when(auctionRepository.existsByKeyboard_Id(any(Long.class))).thenReturn(false);
        when(keyboardRepository.findByIdAndIsDeletedFalse(any(Long.class))).thenReturn(
                Optional.ofNullable(keyboard));
        when(Objects.requireNonNull(keyboard).getMember()).thenReturn(member);
        when(member.getId()).thenReturn(TEST_ID1);
        when(auctionRepository.save(any(Auction.class))).thenReturn(TEST_AUCTION1);

        //when
        AuctionCreateResponseDto actualResponse = auctionService.createAuctionService(TEST_ID1,
                request);

        //then
        assertThat(actualResponse)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectResponse);
    }

    @Test
    @DisplayName("경매 생성 실패 : 해당 키보드로 이미 생성된 경매가 있을 때")
    public void createAuction_fail_already_auction_keyboard() {
        //given
        AuctionCreateRequestDto request = mock(AuctionCreateRequestDto.class);

        when(request.getKeyboardId()).thenReturn(TEST_KEYBOARD_ID1);
        when(auctionRepository.existsByKeyboard_Id(any(Long.class))).thenReturn(true);

        //when&then
        DataDuplicateException e = assertThrows(DataDuplicateException.class, () -> {
            auctionService.createAuctionService(TEST_ID1, request);
        });
        assertEquals("같은 키보드로 여러 개의 경매를 생성할 수 없습니다.", e.getMessage());

    }

    @Test
    @DisplayName("경매 생성 실패 : 해당 키보드가 없을 경우")
    public void createAuction_fail_not_found_keyboard() {
        //given
        AuctionCreateRequestDto request = mock(AuctionCreateRequestDto.class);

        when(request.getKeyboardId()).thenReturn(TEST_KEYBOARD_ID1);
        when(auctionRepository.existsByKeyboard_Id(any(Long.class))).thenReturn(false);
        when(request.getKeyboardId()).thenReturn(TEST_KEYBOARD_ID1);
        when(keyboardRepository.findByIdAndIsDeletedFalse(any(Long.class))).thenReturn(
                Optional.empty());

        //when&then
        DataNotFoundException e = assertThrows(DataNotFoundException.class, () -> {
            auctionService.createAuctionService(TEST_ID1, request);
        });
        assertEquals("해당 키보드을(를) 찾을 수 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("경매 생성 실패 : 본인이 생성한 키보드가 아닐 경우")
    public void createAuction_fail_not_my_keyboard() {
        //given
        AuctionCreateRequestDto request = mock(AuctionCreateRequestDto.class);

        Keyboard keyboard = mock(Keyboard.class);
        Member member = mock(Member.class);

        when(request.getKeyboardId()).thenReturn(TEST_KEYBOARD_ID1);
        when(auctionRepository.existsByKeyboard_Id(any(Long.class))).thenReturn(false);
        when(keyboardRepository.findByIdAndIsDeletedFalse(any(Long.class))).thenReturn(
                Optional.ofNullable(keyboard));
        when(Objects.requireNonNull(keyboard).getMember()).thenReturn(member);
        when(member.getId()).thenReturn(TEST_ID2);

        //when&then
        DataUnauthorizedAccessException e = assertThrows(DataUnauthorizedAccessException.class,
                () -> {
                    auctionService.createAuctionService(TEST_ID1, request);
                });
        assertEquals("접근 권한이 없습니다.", e.getMessage());

    }

}