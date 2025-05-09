package org.team1.keyduck.keyboard.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER2;

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
import org.team1.keyduck.common.exception.DataDuplicateException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataUnauthorizedAccessException;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.keyboard.dto.request.KeyboardCreateRequestDto;
import org.team1.keyduck.keyboard.dto.request.KeyboardUpdateRequestDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardCreateResponseDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardReadResponseDto;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.testdata.TestData;

@ExtendWith(MockitoExtension.class)
class KeyboardServiceTest {

    @Mock
    private KeyboardRepository keyboardRepository;

    @Mock
    private MemberRepository memberRepository;

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
                member.getId())).thenReturn(
                Collections.emptyList());

        //then : 결과검증
        List<KeyboardReadResponseDto> result = keyboardService.findKeyboardBySellerId(TEST_ID1);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("키보드 생성 메서드 - 성공 케이스")
    public void createKeyboardSuccess() {
        // given
        Long memberId = TestData.TEST_ID1;
        Member member = TestData.TEST_MEMBER1;
        Keyboard keyboard = TestData.TEST_KEYBOARD1;

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(keyboardRepository.save(any(Keyboard.class))).thenReturn(keyboard);

        KeyboardCreateRequestDto requestDto = mock(KeyboardCreateRequestDto.class);
        when(requestDto.getName()).thenReturn(keyboard.getName());
        when(requestDto.getDescription()).thenReturn(keyboard.getDescription());

        // when
        KeyboardCreateResponseDto result = keyboardService.createKeyboard(memberId, requestDto);

        // then
        assertThat(result.getName()).isEqualTo(keyboard.getName());
        assertThat(result.getDescription()).isEqualTo(keyboard.getDescription());
    }

    @Test
    @DisplayName("키보드 생성 메서드(실패 케이스) - 유저가 존재하지 않는 경우")
    public void createKeyboardFail() {
        // given
        Long memberId = TestData.TEST_ID1;

        KeyboardCreateRequestDto requestDto = mock(KeyboardCreateRequestDto.class);

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when
        // then
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            keyboardService.createKeyboard(memberId, requestDto);
        });

        assertEquals("해당 멤버을(를) 찾을 수 없습니다.", exception.getMessage());
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

    @Test
    @DisplayName("키보드 삭제 - 성공 케이스")
    void deleteKeyboardSuccess() {
        // given
        Member member = TEST_MEMBER1;
        Keyboard keyboard = TEST_KEYBOARD1;

        ReflectionTestUtils.setField(member, "id", TEST_ID1);
        // false가 삭제되지 않은 상태
        ReflectionTestUtils.setField(keyboard, "isDeleted", false);

        // 키보드 조회하면 설정한 키보드 반환
        when(keyboardRepository.findById(keyboard.getId())).thenReturn(Optional.of(keyboard));

        // when
        keyboardService.deleteKeyboard(keyboard.getId(), member.getId());

        // then
        assertTrue(keyboard.isDeleted());
    }

    @Test
    @DisplayName("키보드 삭제 - 실패 케이스(이미 삭제된 키보드)")
    void deleteKeyboardFailAlreadyDeleted() {
        // given
        Member member = TEST_MEMBER1;
        Keyboard keyboard = TEST_KEYBOARD1;

        // true가 이미 삭제된 상태
        ReflectionTestUtils.setField(keyboard, "isDeleted", true);

        when(keyboardRepository.findById(keyboard.getId())).thenReturn(Optional.of(keyboard));

        // when&then
        DataDuplicateException exception = assertThrows(DataDuplicateException.class, () -> {
            keyboardService.deleteKeyboard(keyboard.getId(), member.getId());
        });

        assertEquals("이미 삭제된 키보드 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("키보드 삭제 - 실패 케이스(생성 유저와 삭제를 하려는 유저가 동일하지 않음)")
    void deleteKeyboardFailNotSameMember() {
        // given
        Member member = mock(Member.class);
        Keyboard keyboard = TEST_KEYBOARD1;

        ReflectionTestUtils.setField(TEST_MEMBER1, "id", TEST_ID1);

        ReflectionTestUtils.setField(keyboard, "isDeleted", false);

        when(keyboardRepository.findById(keyboard.getId())).thenReturn(Optional.of(keyboard));
        when(member.getId()).thenReturn(2L);

        // when&then
        DataUnauthorizedAccessException exception = assertThrows(DataUnauthorizedAccessException.class, () -> {
            keyboardService.deleteKeyboard(keyboard.getId(), member.getId());
        });

        assertEquals("접근 권한이 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("키보드 삭제 - 실패 케이스(경매가 생성된 키보드 삭제 요청)")
    void deleteKeyboardFailAuctionInProgress() {
        // given
        Member member = TEST_MEMBER1;
        Keyboard keyboard = TEST_KEYBOARD1;

        ReflectionTestUtils.setField(member, "id", TEST_ID1);
        ReflectionTestUtils.setField(keyboard, "id", TEST_KEYBOARD_ID1);
        ReflectionTestUtils.setField(keyboard, "isDeleted", false);

        when(keyboardRepository.findById(keyboard.getId())).thenReturn(Optional.of(keyboard));
        when(auctionRepository.existsAuctionByKeyboardId(any(Long.class))).thenReturn(true);

        // when&then
        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            keyboardService.deleteKeyboard(keyboard.getId(), member.getId());
        });

        assertEquals("경매에 등록된 키보드는 삭제할 수 없습니다.", exception.getMessage());
    }
}