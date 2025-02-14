package org.team1.keyduck.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.member.dto.request.MemberUpdatePasswordRequestDto;
import org.team1.keyduck.member.dto.request.MemberUpdateRequestDto;
import org.team1.keyduck.member.dto.response.MemberReadResponseDto;
import org.team1.keyduck.member.dto.response.MemberUpdateResponseDto;
import org.team1.keyduck.member.entity.Address;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;


    @Test
    void 성공_케이스_멤버_업데이트() {
        //given
        Long initMemberId = 1L;
        Address address = new Address("서울시", "강남구", "테헤란로", "상세주소", "상세주소");
        Member beforeMember = new Member("gege", "gege@naver.com", "1234", MemberRole.CUSTOMER,
            address);
        Member expectedMember = new Member("hehe", "heehee@naver.com", "5678", MemberRole.CUSTOMER,
            address);

        MemberUpdateRequestDto requestDto = mock(MemberUpdateRequestDto.class);
        MemberUpdateResponseDto expectMember = MemberUpdateResponseDto.of(expectedMember);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(beforeMember));
        when(requestDto.getName()).thenReturn(expectedMember.getName());
        when(requestDto.getEmail()).thenReturn(expectedMember.getEmail());
        when(requestDto.getAddress()).thenReturn(expectedMember.getAddress());

        //when
        MemberUpdateResponseDto actualMember = memberService.updateMember(requestDto, initMemberId);

        //then
        assertThat(actualMember).usingRecursiveComparison().ignoringFields("id")
            .ignoringFields("createdAt").ignoringFields("modifiedAt").ignoringFields("password")
            .isEqualTo(expectMember);

    }

    @Test
    void 실패_케이스_멤버_업데이트_이메일_형식_불일치() {

        //given
        Long initMemberId = 1L;
        Address address = new Address("서울시", "강남구", "테헤란로", "상세주소", "상세주소");
        Member beforeMember = new Member("gege", "gege@naver.com", "1234", MemberRole.CUSTOMER,
            address);
        Member expectedMember = new Member("hehe", "heer.com", "5678", MemberRole.CUSTOMER,
            address);

        MemberUpdateRequestDto requestDto = mock(MemberUpdateRequestDto.class);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(beforeMember));
        when(requestDto.getName()).thenReturn(expectedMember.getName());
        when(requestDto.getEmail()).thenReturn(expectedMember.getEmail());

        //when&then
        assertThrows(DataNotMatchException.class, () -> {
            memberService.updateMember(requestDto, initMemberId);
        });

    }

    @Test
    void 성공_케이스_비밀번호_업데이트() {
        //given
        Long initMemberId = 1L;
        Address address = new Address("서울시", "강남구", "테헤란로", "상세주소", "상세주소");
        Member beforeMember = new Member("gege", "gege@naver.com", "1234", MemberRole.CUSTOMER,
            address);
        Member expectedMember = new Member("hehe", "heehee@naver.com", "5678", MemberRole.CUSTOMER,
            address);

        MemberUpdatePasswordRequestDto requestDto = mock(MemberUpdatePasswordRequestDto.class);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(beforeMember));
        when(requestDto.getBeforePassword()).thenReturn(beforeMember.getPassword());
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        when(requestDto.getModifyPassword()).thenReturn(expectedMember.getPassword());
        when(passwordEncoder.encode(any(String.class))).thenReturn(expectedMember.getPassword());

        //when
        memberService.updatePassword(requestDto, initMemberId);

        //then
        assertThat(beforeMember.getPassword()).isEqualTo(expectedMember.getPassword());

    }

    @Test
    void 실패_케이스_비밀번호_수정_기존_비밀번호_불일치() {
        //given
        Long initMemberId = 1L;
        Address address = new Address("서울시", "강남구", "테헤란로", "상세주소", "상세주소");
        Member beforeMember = new Member("gege", "gege@naver.com", "1234", MemberRole.CUSTOMER,
            address);
        Member expectedMember = new Member("hehe", "heehee@naver.com", "5678", MemberRole.CUSTOMER,
            address);
        MemberUpdatePasswordRequestDto requestDto = mock(MemberUpdatePasswordRequestDto.class);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(beforeMember));
        when(requestDto.getBeforePassword()).thenReturn(expectedMember.getPassword());

        //when&then
        assertThrows(DataNotMatchException.class, () -> {
            memberService.updatePassword(requestDto, initMemberId);
        });

    }


    @Test
    void 성공_케이스_멤버_조회() {
        //given
        Long initMemberId = 1L;
        Address address = new Address("서울시", "강남구", "테헤란로", "상세주소", "상세주소");
        Member member = new Member("gege", "gege@naver.com", "1234", MemberRole.CUSTOMER, address);
        MemberReadResponseDto expectMember = MemberReadResponseDto.of(member);

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        //when
        MemberReadResponseDto actualMember = memberService.getMember(initMemberId);

        //then
        assertThat(actualMember).usingRecursiveComparison().ignoringFields("id")
            .isEqualTo(expectMember);
    }

}