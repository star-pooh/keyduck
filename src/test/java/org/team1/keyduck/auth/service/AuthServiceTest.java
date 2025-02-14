package org.team1.keyduck.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.team1.keyduck.auth.dto.request.MemberCreateRequestDto;
import org.team1.keyduck.common.exception.DuplicateDataException;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void 멤버_회원가입_성공() {

        //given
        MemberCreateRequestDto requestDto = mock(MemberCreateRequestDto.class);
        Member expectedMember = new Member("hehe", "heehee@naver.com", "1234", MemberRole.CUSTOMER);

        when(requestDto.getEmail()).thenReturn(expectedMember.getEmail());
        when(memberRepository.existsByEmail(any(String.class))).thenReturn(false);

        when(requestDto.getPassword()).thenReturn(expectedMember.getPassword());
        when(passwordEncoder.encode(any(String.class))).thenReturn("5678");

        when(requestDto.getName()).thenReturn(expectedMember.getName());
        when(requestDto.getMemberRole()).thenReturn(expectedMember.getMemberRole());
        when(memberRepository.save(any(Member.class))).thenReturn(expectedMember);

        Member member = mock(Member.class);

        //when
        authService.joinMember(requestDto);
        Member actualMember = memberRepository.save(member);

        //then
        assertThat(actualMember)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .ignoringFields("createdAt")
            .ignoringFields("modifiedAt")
            .isEqualTo(expectedMember);

    }

    @Test
    void 멤버_회원가입_실패_이메일_중복() {

        //given
        MemberCreateRequestDto requestDto = mock(MemberCreateRequestDto.class);

        when(requestDto.getEmail()).thenReturn("heehee@naver.com");
        when(memberRepository.existsByEmail(any(String.class))).thenReturn(true);

        //when&then
        assertThrows(DuplicateDataException.class, () -> {
            authService.joinMember(requestDto);
        });
    }
}