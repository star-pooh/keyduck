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
    void 성공_케이스_멤버_업데이트() {

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
    void 실패_케이스_멤버_업데이트_이메일_형식_불일치() {

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
    void 실패_케이스_이전_이메일과_동일한_이메일로_변경() {

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
    void 실패_케이스_모든_필드가_비어있을경우() {

        //given
        MemberUpdateRequestDto requestDto = new MemberUpdateRequestDto();

        //when&then
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            memberService.updateMember(requestDto, TEST_ID1);
        });

        assertEquals("하나 이상의 수정 내용이 필요합니다.", exception.getMessage());

    }

    @Test
    void 성공_케이스_비밀번호_업데이트() {

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
    void 실패_케이스_비밀번호_이전_비밀번호와_동일함() {

        //given
        Member beforeMember = TEST_MEMBER1;

        MemberUpdatePasswordRequestDto requestDto = mock(MemberUpdatePasswordRequestDto.class);

        when(requestDto.getBeforePassword()).thenReturn(beforeMember.getPassword());
        when(requestDto.getModifyPassword()).thenReturn(TEST_PASSWORD1);

        //when&then
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            memberService.updatePassword(requestDto, TEST_ID1);
        });

        assertEquals("이전 정보와 같은 비밀번호로 변경할 수 없습니다.", exception.getMessage());

    }


}