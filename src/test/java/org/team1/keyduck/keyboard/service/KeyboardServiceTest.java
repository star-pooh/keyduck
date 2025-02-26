package org.team1.keyduck.keyboard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION1;
import static org.team1.keyduck.testdata.TestData.TEST_AUCTION_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD1;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD2;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD3;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER1;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.keyboard.dto.request.KeyboardUpdateRequestDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardReadResponseDto;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.testdata.TestData;

@ExtendWith(MockitoExtension.class)
class KeyboardServiceTest {

    @Mock
    private KeyboardRepository keyboardRepository;

    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private KeyboardService keyboardService;

    @Test
    @DisplayName("키보드 조회_성공_키보드가 1개일때")
    public void findKeyboard_success_one() {
        //given : 설정(테스트준비)
        Member member = TEST_MEMBER1;
        Keyboard keyboard = TEST_KEYBOARD1;

        ReflectionTestUtils.setField(member, "id", TEST_ID1);

        //when : 수행할 작업(테스트 검증을 위한 준비)
        when(keyboardRepository.findAllByMemberIdAndIsDeletedFalseOrderByCreatedAtDesc(
                member.getId())).thenReturn(List.of(keyboard));
        //then : 결과검증
        List<KeyboardReadResponseDto> result = keyboardService.findKeyboardBySellerId(
                member.getId());

        assertEquals(1, result.size());
        assertEquals(keyboard.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("키보드 조회_성공_키보드가 여러개일때")
    public void findKeyboard_success_many() {

        //given : 설정
        Member member = TEST_MEMBER1;

        List<Keyboard> keyboardList = List.of(TestData.TEST_KEYBOARD1, TEST_KEYBOARD2,
                TEST_KEYBOARD3);

        //when : 수행할 작업
        when(keyboardRepository.findAllByMemberIdAndIsDeletedFalseOrderByCreatedAtDesc(
                member.getId())).thenReturn(keyboardList);

        List<KeyboardReadResponseDto> result = keyboardService.findKeyboardBySellerId(
                member.getId());

        //then : 결과검증
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("키보드 조회_키보드가 없을때")
    public void findKeyboard_empty() {

        //given : 설정
        Member member = TEST_MEMBER1;

        //when : 수행할 작업
        when(keyboardRepository.findAllByMemberIdAndIsDeletedFalseOrderByCreatedAtDesc(
                TEST_MEMBER1.getId())).thenReturn(
                Collections.emptyList());

        //then : 결과검증
        List<KeyboardReadResponseDto> result = keyboardService.findKeyboardBySellerId(TEST_ID1);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("키보드 수정_성공")
    public void updateKeyboard_success() {

        //given
        Member member = TEST_MEMBER1;
        Keyboard keyboard = TEST_KEYBOARD1;
        Auction auction = TEST_AUCTION1;
        KeyboardUpdateRequestDto requestDto =
                new KeyboardUpdateRequestDto("이름변경1", "내용변경1");

        ReflectionTestUtils.setField(member, "id", TEST_ID1);
        ReflectionTestUtils.setField(keyboard, "id", TEST_KEYBOARD_ID1);
        ReflectionTestUtils.setField(auction, "id", TEST_AUCTION_ID1);

        //when

        when(keyboardRepository.findById(any(Long.class))).thenReturn(Optional.of(keyboard));
        when(auctionRepository.existsByKeyboard_Member_IdAndAuctionStatus(TEST_KEYBOARD_ID1,
                AuctionStatus.NOT_STARTED)).thenReturn(true);

        keyboardService.keyboardModification(TEST_ID1, TEST_KEYBOARD_ID1, requestDto);

        //then
        assertEquals(keyboard.getName(), "이름변경1");
        assertEquals(keyboard.getDescription(), "내용변경1");

    }

    @Test
    @DisplayName("키보드 수정_실패_키보드 정보 없음")
    public void updateKeyboard_fail_Not_Found_Keyboard() {

    }


}