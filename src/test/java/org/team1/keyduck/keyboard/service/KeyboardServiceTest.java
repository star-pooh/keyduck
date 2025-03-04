package org.team1.keyduck.keyboard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_ID2;
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
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataUnauthorizedAccessException;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
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
    public void findKeyboardSuccessOne() {
        //given : 설정(테스트준비)
        Member member = TEST_MEMBER1;
        Keyboard keyboard = TEST_KEYBOARD1;

        ReflectionTestUtils.setField(member, "id", TEST_ID1);

        when(keyboardRepository.findAllByMemberIdAndIsDeletedFalseOrderByCreatedAtDesc(
                member.getId())).thenReturn(List.of(keyboard));

        //when : 수행할 작업(테스트 검증을 위한 준비)
        List<KeyboardReadResponseDto> result = keyboardService.findKeyboardBySellerId(
                member.getId());

        //then : 결과검증
        assertEquals(1, result.size());
        assertEquals(keyboard.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("키보드 조회_성공_키보드가 여러개일때")
    public void findKeyboardSuccessMany() {

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
    @DisplayName("키보드 조회_성공_키보드가 없을때")
    public void findKeyboardEmpty() {

        //given : 설정
        Member member = TEST_MEMBER1;

        //when : 수행할 작업
        when(keyboardRepository.findAllByMemberIdAndIsDeletedFalseOrderByCreatedAtDesc(
                TEST_MEMBER1.getId())).thenReturn(
                Collections.emptyList());

        List<KeyboardReadResponseDto> result = keyboardService.findKeyboardBySellerId(TEST_ID1);

        //then : 결과검증
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("키보드 수정_성공")
    public void updateKeyboardSuccess() {

        //given
        Keyboard keyboard = Keyboard.builder()
                .name(TestData.TEST_KEYBOARD_NAME1)
                .description(TestData.TEST_KEYBOARD_DESCRIPTION1)
                .build();

        KeyboardUpdateRequestDto requestDto =
                new KeyboardUpdateRequestDto("이름변경1", "내용변경1");

        ReflectionTestUtils.setField(keyboard, "member", TEST_MEMBER1);
        ReflectionTestUtils.setField(TEST_MEMBER1, "id", TEST_ID1);

        //when
        List<AuctionStatus> auctionStatuses = List.of(AuctionStatus.IN_PROGRESS,
                AuctionStatus.CLOSED);

        when(keyboardRepository.findById(any(Long.class))).thenReturn(Optional.of(keyboard));
        when(auctionRepository.existsByKeyboard_IdAndAuctionStatus(TEST_KEYBOARD_ID1,
                auctionStatuses)).thenReturn(false);

        keyboardService.keyboardModification(TEST_ID1, TEST_KEYBOARD_ID1, requestDto);

        //then
        assertEquals(keyboard.getName(), "이름변경1");
        assertEquals(keyboard.getDescription(), "내용변경1");

    }

    @Test
    @DisplayName("키보드 수정_실패_키보드 정보 없음")
    public void updateKeyboardFailNotFoundKeyboard() {
        //given
        KeyboardUpdateRequestDto requestDto =
                new KeyboardUpdateRequestDto("이름변경1", "내용변경1");

        when(keyboardRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //when&then
        DataNotFoundException e = assertThrows(DataNotFoundException.class,
                () -> keyboardService.keyboardModification(TEST_ID1, TEST_KEYBOARD_ID1,
                        requestDto));

        assertEquals("해당 키보드을(를) 찾을 수 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("키보드 수정_실패_키보드를 등록한 셀러가 아님")
    public void updateKeyboardFailUnauthorized() {
        //given
        Keyboard keyboard = mock(Keyboard.class);
        Member member = mock(Member.class);

        KeyboardUpdateRequestDto requestDto = mock(KeyboardUpdateRequestDto.class);

        when(keyboard.getMember()).thenReturn(member);

        when(member.getId()).thenReturn(TEST_ID1);

        when(keyboard.getId()).thenReturn(TEST_KEYBOARD_ID1);

        when(keyboardRepository.findById(any(Long.class))).thenReturn(Optional.of(keyboard));

        //when&then
        DataUnauthorizedAccessException e = assertThrows(DataUnauthorizedAccessException.class,
                () -> keyboardService.keyboardModification(TEST_ID2, keyboard.getId(),
                        requestDto));

        assertEquals("접근 권한이 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("키보드 수정_실패_삭제된 키보드")
    public void updateKeyboardFailIsDeletedKeyboard() {
        //given
        Keyboard keyboard = mock(Keyboard.class);
        Member member = mock(Member.class);

        KeyboardUpdateRequestDto requestDto = mock(KeyboardUpdateRequestDto.class);

        when(keyboard.getMember()).thenReturn(member);

        when(member.getId()).thenReturn(TEST_ID1);

        when(keyboard.getId()).thenReturn(TEST_KEYBOARD_ID1);

        when(keyboardRepository.findById(keyboard.getId())).thenReturn(
                Optional.of(keyboard));

        when(keyboard.isDeleted()).thenReturn(true);

        //when&then
        OperationNotAllowedException e = assertThrows(OperationNotAllowedException.class,
                () -> keyboardService.keyboardModification(TEST_ID1, keyboard.getId(),
                        requestDto));

        assertEquals("삭제된 키보드는 수정할 수 없습니다.", e.getMessage());

    }

    @Test
    @DisplayName("키보드 수정_실패_경매가 시작되었거나, 종료된 키보드")
    public void updateKeyboardFailAuctionStatusWithInProgressAndClosed() {
        //given
        Keyboard keyboard = mock(Keyboard.class);
        Member member = mock(Member.class);

        KeyboardUpdateRequestDto requestDto = mock(KeyboardUpdateRequestDto.class);

        when(keyboard.getMember()).thenReturn(member);

        when(member.getId()).thenReturn(TEST_ID1);

        when(keyboard.getId()).thenReturn(TEST_KEYBOARD_ID1);

        when(keyboardRepository.findById(keyboard.getId())).thenReturn(
                Optional.of(keyboard));

        when(keyboard.isDeleted()).thenReturn(false);
        when(auctionRepository.existsByKeyboard_IdAndAuctionStatus(any(Long.class),
                any())).thenReturn(true);

        //when&then
        OperationNotAllowedException e = assertThrows(OperationNotAllowedException.class,
                () -> keyboardService.keyboardModification(TEST_ID1, keyboard.getId(),
                        requestDto));

        assertEquals("진행 중이거나 종료된 경매는 수정 및 삭제할 수 없습니다.", e.getMessage());

    }

}