package org.team1.keyduck.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_EMAIL1;
import static org.team1.keyduck.testdata.TestData.TEST_EMAIL2;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER1;
import static org.team1.keyduck.testdata.TestData.TEST_NAME1;
import static org.team1.keyduck.testdata.TestData.TEST_PASSWORD1;

import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.team1.keyduck.auth.dto.request.MemberCreateRequestDto;
import org.team1.keyduck.auth.dto.request.SigninRequestDto;
import org.team1.keyduck.common.exception.DataDuplicateException;
import org.team1.keyduck.common.exception.DataInvalidException;
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
    void 판매자_회원가입_성공() {

        //given
        MemberCreateRequestDto requestDto = mock(MemberCreateRequestDto.class);

        when(requestDto.getEmail()).thenReturn(TEST_EMAIL1);
        when(memberRepository.existsByEmail(any(String.class))).thenReturn(false);

        when(requestDto.getPassword()).thenReturn(TEST_PASSWORD1);
        when(passwordEncoder.encode(any(String.class))).thenReturn(TEST_PASSWORD1);

        when(requestDto.getName()).thenReturn(TEST_NAME1);
        when(memberRepository.save(any(Member.class))).thenReturn(TEST_MEMBER1);

        Member member = mock(Member.class);

        //when
        authService.joinMember(requestDto, MemberRole.SELLER);
        Member actualMember = memberRepository.save(member);

        //then
        assertThat(actualMember)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringFields("createdAt")
                .ignoringFields("modifiedAt")
                .isEqualTo(TEST_MEMBER1);

    }

    @Test
    void 멤버_회원가입_실패_이메일_중복() {

        //given
        MemberCreateRequestDto requestDto = mock(MemberCreateRequestDto.class);

        when(requestDto.getEmail()).thenReturn(TEST_EMAIL1);
        when(memberRepository.existsByEmail(any(String.class))).thenReturn(true);

        //when&then
        assertThrows(DataDuplicateException.class, () -> {
            authService.joinMember(requestDto, MemberRole.CUSTOMER);
        });
    }

    @Test
    void 로그인_실패_삭제된_유저() {

        //given
        SigninRequestDto requestDto = mock(SigninRequestDto.class);
        Member member = mock(Member.class);

        when(requestDto.getEmail()).thenReturn(TEST_EMAIL2);
        when(memberRepository.findByEmail(any(String.class))).thenReturn(
                Optional.ofNullable(member));
        when(Objects.requireNonNull(member).isDeleted()).thenReturn(true);

        //when&then
        assertThrows(DataInvalidException.class, () -> {
            authService.login(requestDto);
        });

    }
}