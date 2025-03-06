package org.team1.keyduck.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_ADDRESS1;
import static org.team1.keyduck.testdata.TestData.TEST_ADDRESS2;
import static org.team1.keyduck.testdata.TestData.TEST_EMAIL1;
import static org.team1.keyduck.testdata.TestData.TEST_EMAIL2;
import static org.team1.keyduck.testdata.TestData.TEST_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER1;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER_ROLE1;
import static org.team1.keyduck.testdata.TestData.TEST_NAME1;
import static org.team1.keyduck.testdata.TestData.TEST_NAME2;
import static org.team1.keyduck.testdata.TestData.TEST_PASSWORD1;
import static org.team1.keyduck.testdata.TestData.TEST_PASSWORD3;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.common.service.CommonService;
import org.team1.keyduck.member.dto.request.MemberUpdatePasswordRequestDto;
import org.team1.keyduck.member.dto.request.MemberUpdateRequestDto;
import org.team1.keyduck.member.dto.response.MemberUpdateResponseDto;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    CommonService commonService;

    @Mock
    PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("멤버 업데이트 성공")
    void memberUpdateSuccess() {

        //given
        MemberUpdateRequestDto requestDto = mock(MemberUpdateRequestDto.class);
        Member beforeModifyMember = new Member(TEST_NAME1, TEST_EMAIL1, TEST_PASSWORD1,
                TEST_MEMBER_ROLE1,
                TEST_ADDRESS1);
        Member expectMember = new Member(TEST_NAME2, TEST_EMAIL2, TEST_PASSWORD1, TEST_MEMBER_ROLE1,
                TEST_ADDRESS2);
        MemberUpdateResponseDto expectResponse = MemberUpdateResponseDto.of(expectMember);

        when(memberRepository.findById(any(Long.class))).thenReturn(
                Optional.of(beforeModifyMember));
        when(requestDto.getName()).thenReturn(TEST_NAME2);
        when(requestDto.getEmail()).thenReturn(TEST_EMAIL2);
        when(requestDto.getAddress()).thenReturn(TEST_ADDRESS2);

        //when
        MemberUpdateResponseDto actualMember = memberService.updateMember(requestDto, TEST_ID1);

        //then
        assertThat(actualMember).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectResponse);

    }

    @Test
    @DisplayName("멤버 업데이트 실패 : 이메일 형식 불일치")
    void memberUpdateFailEmailInvalid() {

        //given
        MemberUpdateRequestDto requestDto = mock(MemberUpdateRequestDto.class);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(TEST_MEMBER1));
        when(requestDto.getName()).thenReturn(TEST_NAME1);
        when(requestDto.getEmail()).thenReturn("ASDDFFGGHH");

        //when&then
        DataNotMatchException exception = assertThrows(DataNotMatchException.class, () -> {
            memberService.updateMember(requestDto, TEST_ID1);
        });
        assertEquals("유효하지 않은 이메일 입니다.", exception.getMessage());

    }

    @Test
    @DisplayName("멤버 업데이트 실패 : 이전 이메일과 동일한 이메일로 변경")
    void memberUpdateFailDuplicateEmail() {

        //given
        MemberUpdateRequestDto requestDto = mock(MemberUpdateRequestDto.class);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(TEST_MEMBER1));
        when(requestDto.getName()).thenReturn(TEST_NAME1);
        when(requestDto.getEmail()).thenReturn(TEST_EMAIL1);

        //when&then
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            memberService.updateMember(requestDto, TEST_ID1);
        });

        assertEquals("이전 정보와 같은 이메일로 변경할 수 없습니다.", exception.getMessage());

    }

    @Test
    @DisplayName("멤버 업데이트 실패 : 모든 필드가 비어있을 경우")
    void memberUpdateFailAllFieldEmpty() {

        //given
        MemberUpdateRequestDto requestDto = new MemberUpdateRequestDto();

        //when&then
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            memberService.updateMember(requestDto, TEST_ID1);
        });

        assertEquals("하나 이상의 수정 내용이 필요합니다.", exception.getMessage());

    }

    @Test
    @DisplayName("멤버 비밀번호 업데이트 성공")
    void memberPasswordUpdateSuccess() {

        //given
        Member beforeMember = TEST_MEMBER1;

        MemberUpdatePasswordRequestDto requestDto = mock(MemberUpdatePasswordRequestDto.class);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(beforeMember));
        when(requestDto.getBeforePassword()).thenReturn(beforeMember.getPassword());
        when(requestDto.getModifyPassword()).thenReturn(TEST_PASSWORD3);
        when(passwordEncoder.encode(any(String.class))).thenReturn(TEST_PASSWORD3);

        //when
        memberService.updatePassword(requestDto, TEST_ID1);

        //then
        assertThat(beforeMember.getPassword()).isEqualTo(TEST_PASSWORD3);

    }

    @Test
    @DisplayName("멤버 비밀번호 업데이트 실패 : 수정하려는 비밀번호가 이전 비밀번호와 동일함")
    void memberPasswordUpdateFailModifyPasswordAndBeforePasswordIsSame() {

        //given

        MemberUpdatePasswordRequestDto requestDto = mock(MemberUpdatePasswordRequestDto.class);

        when(requestDto.getBeforePassword()).thenReturn(TEST_MEMBER1.getPassword());
        when(requestDto.getModifyPassword()).thenReturn(TEST_PASSWORD1);

        //when&then
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            memberService.updatePassword(requestDto, TEST_ID1);
        });

        assertEquals("이전 정보와 같은 비밀번호로 변경할 수 없습니다.", exception.getMessage());

    }


}