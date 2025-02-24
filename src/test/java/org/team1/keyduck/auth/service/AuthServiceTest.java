package org.team1.keyduck.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_EMAIL1;
import static org.team1.keyduck.testdata.TestData.TEST_PASSWORD1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.team1.keyduck.auth.dto.request.MemberCreateRequestDto;
import org.team1.keyduck.common.exception.DuplicateDataException;
import org.team1.keyduck.member.entity.Address;
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
    void 고객_회원가입_성공() {

        //given
        MemberCreateRequestDto requestDto = mock(MemberCreateRequestDto.class);
        Address address = new Address("서울시", "강남구", "테헤란로", "상세주소", "상세주소");
        Member expectedMember = new Member("hehe", "heehee@naver.com", "1234", MemberRole.CUSTOMER,
                address);

        when(requestDto.getEmail()).thenReturn(TEST_EMAIL1);
        when(memberRepository.existsByEmail(any(String.class))).thenReturn(false);

        when(requestDto.getPassword()).thenReturn(TEST_PASSWORD1);
        when(passwordEncoder.encode(any(String.class))).thenReturn(TEST_PASSWORD1);

        when(requestDto.getName()).thenReturn(expectedMember.getName());
        when(memberRepository.save(any(Member.class))).thenReturn(expectedMember);

        Member member = mock(Member.class);

        //when
        authService.joinMember(requestDto, MemberRole.CUSTOMER);
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